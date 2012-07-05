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

/**
 * <p>
 * Implements the basic Dijkstra shortest path algorithm. By passing in different relaxers, the algorithm can be
 * tweaked.
 * </p>
 *
 * @author Karel Maesen
 * @author Jeroen Vuerinckx
 */
public class Dijkstra<N, E> implements GraphAlgorithm<Path<N>> {
    private DijkstraAlgorithm<N, E> dijkstraAlgorithm;
    private Path<N> result;

    protected Dijkstra(Graph<N, E> graph, N origin, N destination, Relaxer<N, E> relaxer, int weightIndex,
                       RoutingContextualReachability<N, E, Traversal<N, E>> reachability) {
        reachability.setOriginDestination(
                graph.getInternalNode(origin).getWrappedNode(),
                graph.getInternalNode(destination).getWrappedNode());

        final InternalNode<N, E> destinationInternalNode = graph.getInternalNode(destination);

        dijkstraAlgorithm = new DijkstraAlgorithm<N, E>(graph, origin, relaxer, weightIndex, reachability) {
            @Override
            protected boolean isDone(PredGraph<N, E> pu) {
                if (pu.getInternalNode().equals(destinationInternalNode)) {
                    result = toPath(pu);
                    return true;
                }
                return false;
            }
        };
    }

    protected Dijkstra(Graph<N, E> graph, N origin, N destination, Relaxer<N, E> relaxer, int weightIndex) {
        this(graph, origin, destination, relaxer, weightIndex, new EmptyContextualReachability<N, E, Traversal<N, E>>());
    }

    public void execute() {
        dijkstraAlgorithm.execute();
    }

    private Path<N> toPath(PredGraph<N, E> p) {
        BasicPath<N> path = new BasicPath<N>();
        path.setTotalWeight(p.getWeight());
        path.insert(p.getInternalNode().getWrappedNode());
        PredGraph<N, E> next = p.getPredecessor();

        while (next != null) {
            path.insert(next.getInternalNode().getWrappedNode());
            next = next.getPredecessor();
        }
        path.setValid(true);
        return path;
    }

    public Path<N> getResult() {
        return this.result;
    }
}
