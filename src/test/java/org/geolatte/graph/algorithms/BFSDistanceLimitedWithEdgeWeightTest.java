package org.geolatte.graph.algorithms;

import org.geolatte.graph.*;
import org.geolatte.stubs.MyLocatableNode;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * <p>
 * Tests the {@link org.geolatte.graph.algorithms.BFSDistanceLimited} class when used with different edge weights.
 * </p>
 *
 * @author Jeroen Vuerinckx
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 */
public class BFSDistanceLimitedWithEdgeWeightTest {

    /**
     * We use this graph for testing.
     * edge weights: 1
     * cumulative node weights: (1)
     * node ids: _1
     * all edges are added for both directions
     *
     *   _0                 _3
     *   o--------10-------(3)
     *    \                /
     *     1              1
     *      \            /
     *      _1         _2
     *      (1)---1---(2)
     *
     */

    private MyLocatableNode _0  = new MyLocatableNode(0, 0, 0);
    private MyLocatableNode _1  = new MyLocatableNode(1, 0, 0);
    private MyLocatableNode _2  = new MyLocatableNode(2, 0, 0);
    private MyLocatableNode _3  = new MyLocatableNode(3, 0, 0);

    private Graph<MyLocatableNode, Object> graph;

    @Before
    public void setup() throws Exception {

        Extent extent = new Extent(0d, 0d, 200d, 200d);

        GraphBuilder<MyLocatableNode, Object> builder = Graphs.createGridIndexedGraphBuilder(extent, 10);

        builder.addEdge(_0, _3, new BasicEdgeWeight(10));
        builder.addEdge(_0, _1, new BasicEdgeWeight(1));
        builder.addEdge(_1, _2, new BasicEdgeWeight(1));
        builder.addEdge(_2, _3, new BasicEdgeWeight(1));

        // Add same edges in other direction
        builder.addEdge(_3, _0, new BasicEdgeWeight(10));
        builder.addEdge(_1, _0, new BasicEdgeWeight(1));
        builder.addEdge(_2, _1, new BasicEdgeWeight(1));
        builder.addEdge(_3, _2, new BasicEdgeWeight(1));

        graph = builder.build();
    }

    @Test
    public void testExecute() {

        int maxDistance = 10;
        GraphAlgorithm<GraphTree<MyLocatableNode, Object>> bfsAlgorithm = GraphAlgorithms.createBFS(graph, _0, maxDistance, 0);
        bfsAlgorithm.execute();
        GraphTree<MyLocatableNode, Object> result = bfsAlgorithm.getResult();

        assertEquals(4, result.toMap().size());
        result.toMap().keySet().containsAll(Arrays.asList(_0, _1, _2, _3));

        assertEquals(result.toMap().get(_3), 3., 0);

    }

}
