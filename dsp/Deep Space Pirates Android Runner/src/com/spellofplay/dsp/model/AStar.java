package com.spellofplay.dsp.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AStar {
	
	public List<ModelPosition> m_path;
	public SearchResult m_state;
	private boolean m_canMoveThroughObstacles = false;
	//private boolean m_doingFirstSearchWithObstacles = true;
	
	public AStar(IMoveAndVisibility a_map) 
    {
        m_map = a_map;
        m_path = new ArrayList<ModelPosition>();
        m_state = SearchResult.SearchNotStarted;
    }

    public enum SearchResult
    {
        SearchNotStarted,
        SearchFailedNoPath,
        SearchNotDone,
        SearchSucceded
    };
    
    
    
    public SearchResult Update(int a_maxNodes)
    {
        if (m_state != SearchResult.SearchNotDone)
        {
            return m_state;
        }
        if (m_doesNearSearch == false && m_map.isMovePossible(m_end, m_canMoveThroughObstacles) == false)
        {
        	return doFailedSearch();
        }
        m_nVisitedNodes = 0;

        while(m_listOpen.size() > 0)
        {
            if (m_nVisitedNodes > a_maxNodes) {
                m_state = SearchResult.SearchNotDone;
                return SearchResult.SearchNotDone;
            }
            SearchResult result = OneSearchStep();
            if (result != SearchResult.SearchNotDone)
            {
                m_state = result;
                return result;
            }
        }
        return doFailedSearch();
    }

	private SearchResult doFailedSearch() {
		if (m_canMoveThroughObstacles) {
			//reset
			InitSearch(m_start, m_end, m_doesNearSearch, m_nearDistance, false);
			m_canMoveThroughObstacles = false;
			return SearchResult.SearchNotDone;
		}
		m_state = SearchResult.SearchFailedNoPath;
		return SearchResult.SearchFailedNoPath;
	}
    
    public void InitSearch(ModelPosition a_start, ModelPosition a_end, boolean a_nearSearch, float a_distance, boolean a_canMoveThroughObstacles)
    {
        m_canMoveThroughObstacles = a_canMoveThroughObstacles;
        
        m_path = new ArrayList<ModelPosition>();
        m_doesNearSearch = a_nearSearch;
        m_nearDistance = a_distance;
        m_nVisitedNodes = 0;
        m_state = SearchResult.SearchNotDone;
        Node startNode = new Node();
        startNode.m_nCostFromstart = 0.0f;
        startNode.m_nCostToGoal = TraverseCost(a_start, a_end);
        startNode.m_node = a_start;
        startNode.m_parent = null;
        
        m_listOpen = new ArrayList<Node>();
        m_listClosed = new ArrayList<Node>();

        m_listOpen.add(startNode);
        
        m_start = a_start;
        m_end = a_end;
    }
    
    
	
	private class Node implements Comparable<Node>
	{
		public Node() {
			m_parent = null;
			m_nCostFromstart = 0;
			m_nCostToGoal = 0;
		}
		public ModelPosition m_node = new ModelPosition();
		public float m_nCostFromstart;
		public float m_nCostToGoal;
		public Node	m_parent;
		
        public boolean isSameLocation(Node other) {
        	return m_node.equals(other.m_node);
        		
        	
        }

		@Override
		public int compareTo(Node arg0) {
			
			if (arg0 == null)
				throw new NullPointerException();
			
			Node other = (Node)arg0;
			
			int res = (int)((m_nCostFromstart + m_nCostToGoal) - (other.m_nCostFromstart + other.m_nCostToGoal));
			if (res > 0)
				return 1;
			else if (res < 0)
				return -1;
			
			return 0; 
		}
	};

    private static final float m_heuristicsModifier = 1.5f;
    private boolean m_doesNearSearch;
    private float m_nearDistance;
    private int m_nVisitedNodes;
    private float m_fTravelDistant;
	        
    private List<Node> m_listOpen;
    private List<Node> m_listClosed;
	
    private IMoveAndVisibility m_map;
    private ModelPosition m_start, m_end;
	     
	private float TraverseCost(ModelPosition a_a, ModelPosition a_b) {
        return a_a.sub(a_b).length();
    }

	private void CreatePath(Node a_node) {
        if (a_node.m_parent != null) {
	        CreatePath(a_node.m_parent);
        }
        if (a_node.m_node.equals(m_start)) {
    		
        } else {
    		
	        m_path.add(a_node.m_node);
        }
    }


	private SearchResult OneSearchStep()
    {

        //Inga flera noder att söka igenom
        if (m_listOpen.size() == 0) {
	        return SearchResult.SearchFailedNoPath;
        }
        //Hämta första noden i open
        Node pNode = m_listOpen.get(0);
        m_listOpen.remove(0);
    	
        //Har vi nått fram
        if (IsAGoalNode(pNode)) {
        	m_path = new ArrayList<ModelPosition>();
	        CreatePath(pNode);
	        m_fTravelDistant = pNode.m_nCostFromstart;

	        return SearchResult.SearchSucceded;
        } else {
	        //Stega igenom nodens barn
	        for (int y = -1; y <= 1; y++) {
		        for (int x = -1; x <= 1; x++) {
    			

			        if (x == 0 && y == 0) 
				        continue;

			        // handle diagonal, should be like this in dungeon but not on trail...
			        // on trail diagonal is ok 
			        if (x == y || x == -y) {
				        if (m_map.isMovePossible(new ModelPosition(pNode.m_node.m_x + x, pNode.m_node.m_y), true) == false)
					        continue;
				        if (m_map.isMovePossible(new ModelPosition(pNode.m_node.m_x, pNode.m_node.m_y + y), true) == false)
					        continue;
			        }


                    if (m_map.isMovePossible(new ModelPosition(pNode.m_node.m_x + x, pNode.m_node.m_y + y), m_canMoveThroughObstacles) == true)
                    {
				        Node NewNode = new Node();
				        
				        
				        NewNode.m_parent = null;
				        NewNode.m_node.m_x = pNode.m_node.m_x + x;
				        NewNode.m_node.m_y = pNode.m_node.m_y + y;

				        //Kostnaden för att gå från start till barnnoden
				        float dNewCost = pNode.m_nCostFromstart + TraverseCost(pNode.m_node, NewNode.m_node);

				        //Är den nya kostnaden bättre än någon vi hittat förut till barnnoden?
				        boolean bImprovment = IsImprovment(NewNode, dNewCost);
    					
				        //Om den finns i köerna och inte är en förbättring slänger vi den...
				        if (!bImprovment && (ExistInOpen(NewNode.m_node) || ExistInClosed(NewNode.m_node))) {
					        continue;
				        } else {
					        m_nVisitedNodes++;

					        //Vilken väg tog vi för att komma till noden
					        NewNode.m_parent = pNode;
					        //NewNode.m_pNode->setParentNode(node.m_pNode);
					        NewNode.m_nCostFromstart = dNewCost;

					        //Estimera kostnaden till målet
					        NewNode.m_nCostToGoal = TraverseCost(NewNode.m_node, m_end) * m_heuristicsModifier;
    						
					        //om den fanns i closed plocka upp den igen...
					        //ta bort den ur closed
					        //if (ExistInClosed(NewNode.m_node)) {
						    removeFromClosed(NewNode);
					        //}
					        //om den fanns i open plocka upp den igen...
					        //ta bort den ur closed
					        //if (ExistInOpen( NewNode.m_node)) {
						    removeFromOpen(NewNode);
					        //}
					        //och lägg den längst bak i open
					        m_listOpen.add(NewNode);
				        }
			        }
		        }
	        }
	        
	        try {
		        //sortera listan
		        Collections.sort(m_listOpen);
	        } catch (Exception e) {
	        	
	        }
	        
        }

        m_listClosed.add(pNode);
        return SearchResult.SearchNotDone;
    }

	

	private boolean IsAGoalNode(Node pNode)
    {
        if (m_doesNearSearch == false) {
	        return pNode.m_node.equals(m_end);
        }
        else
        {
            return pNode.m_node.sub(m_end).length() <= m_nearDistance;
        }
    }



    

    private boolean ExistInOpen(ModelPosition a_node) {
        for (Node iter : m_listOpen) {
	        if (iter.m_node.equals(a_node)) {
		        return true;
	        }
        }
        return false;
    }

    private boolean ExistInClosed(ModelPosition a_node) {
    	for (Node iter : m_listClosed) {
	        if (iter.m_node.equals(a_node)) {
		        return true;
	        }
        }
        return false;
    }
    
    private void removeFromOpen(Node NewNode) {
		boolean change = true;
		while (change)
		{
		    change = false;
		    for( Node iter : m_listOpen)
		    {
		        if (iter.isSameLocation(NewNode))
		        {
		            m_listOpen.remove(iter);
			        change = true;
			        break;
		        }
		    }
		}
	}

	private void removeFromClosed(Node NewNode) {
		boolean change = true;
		while (change) {
		    change = false;
		    
		    for(Node iter : m_listClosed)
		    {
		        if (iter.isSameLocation(NewNode))
		        {
		            m_listClosed.remove(iter);
			        change = true;
			        break;
		        }
		    }
		}
	}

	private boolean IsImprovment(Node a_nNode, double a_dVal)
    {

    	boolean foundImprovment = false;

       
        for(Node iter : m_listOpen)
        {
	        if (iter.m_node.equals(a_nNode.m_node))
	        {
		        if (a_dVal < iter.m_nCostFromstart)
		        {
			        //Vi har hittat en bättre väg till noden...
			        foundImprovment = true;
		        }
	        }
        }
    	
        //iter = m_listClosed.begin();
        //for (; iter != m_listClosed.end(); iter++)
        for(Node iter : m_listClosed)
        {
	        if (iter.m_node.equals(a_nNode.m_node))
	        {
		        if (a_dVal < iter.m_nCostFromstart)
		        {
			        foundImprovment = true;
		        }
	        }
        }
        return foundImprovment;
    }

    
}
	
