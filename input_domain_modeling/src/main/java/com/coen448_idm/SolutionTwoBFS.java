package coen448.assignment1;

import java.util.Stack;
public class SolutionTwoBFS {
    public boolean isBipartite(int[][] graph) {
    
    
            int[] visited =new int[graph.length]; // associated array for visiting and coloring
                
            Stack<Integer> stack = new Stack<Integer>(); //keeping information of the nodes; 
                
            for(int i = 0;i<graph.length;i++) {
                    visited[i] = 0;  // 0 is undefined for coloring 
            }    
                

            for (int i = 0; i < graph.length; i++) { // traverse the whole graph
                if (visited[i]!=0) continue;          // skipped colored
                stack.push(i);     // put the vertex in the stack
                visited[i] = 1;              // colored blue;

                while (stack.size()>0){       // get all the neigbors of each vertex in the queue
                    int cur=stack.pop();          
                    int curColor = visited[cur]; //the current color
                        
                    int neighborColor = -curColor;     // the neighber should be of different color

                    for (int j = 0; j < graph[cur].length; j++) { //color all the neighbours
                        int neighbor = graph[cur][j];
                        if (visited[neighbor] == 0){        // undefined, not colored yet
                            visited[neighbor] = neighborColor;         // color it differently
                            stack.push(neighbor);                      // put it to the stack
                        } else if (visited[neighbor] != neighborColor) { // have the same color with its neighour
                            return false;
                        }
                    }
                }
            }
    return true; 
}
    
}