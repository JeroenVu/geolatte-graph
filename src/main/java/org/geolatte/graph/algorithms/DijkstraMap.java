/*
 * This file is part of the GeoLatte project.
 *
 *     GeoLatte is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     GeoLatte is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with GeoLatte.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (C) 2010 - 2011 and Ownership of code is shared by:
 * Qmino bvba - Esperantolaan 4 - 3001 Heverlee  (http://www.qmino.com)
 * Geovise bvba - Generaal Eisenhowerlei 9 - 2140 Antwerpen (http://www.geovise.com)
 */

package org.geolatte.graph.algorithms;

import org.geolatte.graph.*;

import java.util.HashMap;

/**
 * <p>
 * Implements the basic Dijkstra shortest path algorithm. By passing in different relaxers, the algorithm can be
 * tweaked. This implementation returns a map of the nodes within the given distance and the length of the shortest path
 * to that node.
 * </p>
 *
 * @author Jeroen Vuerinckx
 */
public class DijkstraMap<N, E> implements GraphAlgorithm<HashMap<N, Double>> {
    private DijkstraAlgorithm<N, E> dijkstraAlgorithm;
    private HashMap<N, Double> result;

    protected DijkstraMap(Graph<N, E> graph, N origin, final float maxDistance, Relaxer<N, E> relaxer, int weightIndex,
                          ContextualReachability<N, E, Traversal<N, E>> reachability) {
        dijkstraAlgorithm = new DijkstraAlgorithm<N, E>(graph, origin, relaxer, weightIndex, reachability) {
            @Override
            protected boolean isDone(PredGraph<N, E> pu) {
                return pu.getWeight() > maxDistance;
            }

            @Override
            protected void weightUpdated(PredGraph<N, E> pv) {
                if (pv.getWeight() <= maxDistance) {
                    result.put(pv.getInternalNode().getWrappedNode(), (double) pv.getWeight());
                }
            }
        };
    }

    protected DijkstraMap(Graph<N, E> graph, N origin, float maxDistance, Relaxer<N, E> relaxer, int weightIndex) {
        this(graph, origin, maxDistance, relaxer, weightIndex, new EmptyContextualReachability<N, E, Traversal<N, E>>());
    }

    public void execute() {
        result = new HashMap<N, Double>();
        result.put(dijkstraAlgorithm.getOrigin().getWrappedNode(), 0d);

        dijkstraAlgorithm.execute();
    }

    public HashMap<N, Double> getResult() {
        return this.result;
    }

}