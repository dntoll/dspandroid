package com.spellofplay.dsp.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class AStar {
	public enum SearchResult
    {
    	SearchNotDone,
        SearchFailedNoPath,
        SearchSucceded
    };
    
	public List<ModelPosition> path;
	private SearchResult state;
	private static final float HEURISTICSMODIFIER = 1.5f;
    private boolean isNearSearch;
    private float nearDistance;
    private int numberOfVisitedNodes;
    private List<Node> openList;
    private List<Node> closedList;
	
    private IMoveAndVisibility movementAndVisibility;
    private ModelPosition startPosition, goalPosition;
	
	public AStar(IMoveAndVisibility a_map) 
    {
        movementAndVisibility = a_map;
        path = new ArrayList<ModelPosition>();
        state = SearchResult.SearchNotDone;
    }

    
    
    public boolean isSearchDone() {
    	return state == SearchResult.SearchSucceded;
	}
    
    public boolean isSearching() {
		return state == SearchResult.SearchNotDone;
	}
    
    public boolean didSearchFail() {
    	return state == SearchResult.SearchFailedNoPath;
	}
    
    public SearchResult Update(int a_maxNodes)
    {
        if (state != SearchResult.SearchNotDone)
        {
            return state;
        }
        if (isNearSearch == false && movementAndVisibility.isMovePossible(goalPosition) == false)
        {
        	return doFailedSearch();
        }
        numberOfVisitedNodes = 0;

        while(openList.size() > 0)
        {
            if (numberOfVisitedNodes > a_maxNodes) {
                state = SearchResult.SearchNotDone;
                return SearchResult.SearchNotDone;
            }
            SearchResult result = OneSearchStep();
            if (result != SearchResult.SearchNotDone)
            {
                state = result;
                return result;
            }
        }
        return doFailedSearch();
    }

	private SearchResult doFailedSearch() {
		state = SearchResult.SearchFailedNoPath;
		return SearchResult.SearchFailedNoPath;
	}
    
    public void InitSearch(ModelPosition a_start, 
    					   ModelPosition a_end, 
    					   boolean a_nearSearch, 
    					   float a_distance)
    {
        
        path = new ArrayList<ModelPosition>();
        isNearSearch = a_nearSearch;
        nearDistance = a_distance;
        numberOfVisitedNodes = 0;
        state = SearchResult.SearchNotDone;
        Node startNode = new Node();
        startNode.m_nCostFromstart = 0.0f;
        startNode.m_nCostToGoal = TraverseCost(a_start, a_end);
        startNode.m_node = a_start;
        startNode.m_parent = null;
        
        openList = new ArrayList<Node>();
        closedList = new ArrayList<Node>();

        openList.add(startNode);
        
        startPosition = a_start;
        goalPosition = a_end;
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

    
	     
	private float TraverseCost(ModelPosition a_a, ModelPosition a_b) {
        return a_a.sub(a_b).length();
    }

	private void CreatePath(Node a_node) {
        if (a_node.m_parent != null) {
	        CreatePath(a_node.m_parent);
        }
        if (a_node.m_node.equals(startPosition)) {
    		
        } else {
    		
	        path.add(a_node.m_node);
        }
    }


	private SearchResult OneSearchStep()
    {

        //Inga flera noder att söka igenom
        if (openList.size() == 0) {
	        return SearchResult.SearchFailedNoPath;
        }
        //Hämta första noden i open
        Node pNode = openList.get(0);
        openList.remove(0);
    	
        //Har vi nått fram
        if (IsAGoalNode(pNode)) {
        	path = new ArrayList<ModelPosition>();
	        CreatePath(pNode);
	        return SearchResult.SearchSucceded;
        } else {
	        //Stega igenom nodens barn
	        for (int y = -1; y <= 1; y++) {
		        for (int x = -1; x <= 1; x++) {
    			

			        visitNeighbours(pNode, y, x);
		        }
	        }
	        
	        try {
		        //sortera listan
		        Collections.sort(openList);
	        } catch (Exception e) {
	        	
	        }
	        
        }

        closedList.add(pNode);
        return SearchResult.SearchNotDone;
    }

	private void visitNeighbours(Node pNode, int y, int x) {
		if (x == 0 && y == 0)
			return;

		if ( canMoveDiagonal(pNode, y, x) == false) {
			return;
		}


		if (movementAndVisibility.isMovePossible(new ModelPosition(pNode.m_node.x + x, pNode.m_node.y + y)) == true)
		{
		    Node NewNode = new Node();
		    
		    
		    NewNode.m_parent = null;
		    NewNode.m_node.x = pNode.m_node.x + x;
		    NewNode.m_node.y = pNode.m_node.y + y;

		    //Kostnaden för att gå från start till barnnoden
		    float dNewCost = pNode.m_nCostFromstart + TraverseCost(pNode.m_node, NewNode.m_node);

		    //Är den nya kostnaden bättre än någon vi hittat förut till barnnoden?
		    boolean bImprovment = IsImprovment(NewNode, dNewCost);
			
		    //Om den finns i köerna och inte är en förbättring slänger vi den...
		    if (!bImprovment && (ExistInOpen(NewNode.m_node) || ExistInClosed(NewNode.m_node))) {
		        return;
		    } else {
		        visitNode(pNode, NewNode, dNewCost);
		    }
		}
	}

	private boolean canMoveDiagonal(Node pNode, int y, int x) {
		if (x == y || x == -y) {
		    if (movementAndVisibility.isMovePossible(new ModelPosition(pNode.m_node.x + x, pNode.m_node.y)) == false)
				return false;
		    if (movementAndVisibility.isMovePossible(new ModelPosition(pNode.m_node.x, pNode.m_node.y + y)) == false)
				return false;
		}
		return true;
	}

	private void visitNode(Node pNode, Node NewNode, float dNewCost) {
		numberOfVisitedNodes++;

		NewNode.m_parent = pNode;
		NewNode.m_nCostFromstart = dNewCost;
		NewNode.m_nCostToGoal = TraverseCost(NewNode.m_node, goalPosition) * HEURISTICSMODIFIER;
		removeFromClosed(NewNode);
		removeFromOpen(NewNode);
		openList.add(NewNode);
	}

	

	private boolean IsAGoalNode(Node pNode)
    {
        if (isNearSearch == false) {
	        return pNode.m_node.equals(goalPosition);
        }
        else
        {
            return pNode.m_node.sub(goalPosition).length() <= nearDistance &&
            movementAndVisibility.hasClearSight(pNode.m_node, goalPosition);
        }
    }



    

    private boolean ExistInOpen(ModelPosition a_node) {
        for (Node iter : openList) {
	        if (iter.m_node.equals(a_node)) {
		        return true;
	        }
        }
        return false;
    }

    private boolean ExistInClosed(ModelPosition a_node) {
    	for (Node iter : closedList) {
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
		    for( Node iter : openList)
		    {
		        if (iter.isSameLocation(NewNode))
		        {
		            openList.remove(iter);
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
		    
		    for(Node iter : closedList)
		    {
		        if (iter.isSameLocation(NewNode))
		        {
		            closedList.remove(iter);
			        change = true;
			        break;
		        }
		    }
		}
	}

	private boolean IsImprovment(Node a_nNode, double a_dVal)
    {

    	boolean foundImprovment = false;

       
        for(Node iter : openList)
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

        for(Node iter : closedList)
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
	
