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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public abstract class DijkstraAlgorithm<N, E> {
    private Graph<N, E> graph;
    private InternalNode<N, E> origin;
    private Relaxer<N, E> relaxer;
    private int weightIndex;
    private ContextualReachability<N, E, Traversal<N, E>> reachability;

    public DijkstraAlgorithm(
            Graph<N, E> graph,
            N origin,
            Relaxer<N, E> relaxer,
            int weightIndex,
            ContextualReachability<N, E, Traversal<N, E>> reachability) {
        this.graph = graph;
        this.origin = graph.getInternalNode(origin);
        this.relaxer = relaxer;
        this.weightIndex = weightIndex;
        this.reachability = reachability;
    }

    public void execute() {
        PMinQueue<N, E> minQueue = new PMinQueue<N, E>();
        Set<InternalNode<N, E>> closed = new HashSet<InternalNode<N, E>>();
        BasicPredGraph<N, E> startPG = new BasicPredGraph<N, E>(origin, 0.0f);
        minQueue.add(startPG, Float.POSITIVE_INFINITY);
        while (!minQueue.isEmpty()) {
            PredGraph<N, E> pu = minQueue.extractMin();
            closed.add(pu.getInternalNode());
            if (isDone(pu)) {
                return;
            }
            InternalNode<N, E> u = pu.getInternalNode();
            reachability.setContext(pu);
            Iterator<InternalNode<N, E>> outEdges = graph.getOutGoingEdges(u, reachability);
            while (outEdges.hasNext()) {
                InternalNode<N, E> v = outEdges.next();
                if (closed.contains(v)) {
                    continue;
                }
                PredGraph<N, E> pv = minQueue.get(v);
                if (pv == null) {
                    pv = new BasicPredGraph<N, E>(v, Float.POSITIVE_INFINITY);
                    minQueue.add(pv, Float.POSITIVE_INFINITY);
                }
                if (relaxer.relax(pu, pv, weightIndex)) {
                    minQueue.update(pv, relaxer.newTotalWeight());
                    weightUpdated(pv);
                }
            }
        }
    }

    /**
     * Allow implementations to stop the algorithm, based on the current PredGraph node.
     *
     * @param pu            The current PredGraph node.
     * @return false        As default, do not stop until all nodes are done.
     */
    protected boolean isDone(PredGraph<N, E> pu) {
        return false;
    }

    /**
     * Allow implementations to do something when the weight of a node is updated.
     *
     * @param pv            The PredGraph of which the weight was updated.
     */
    protected void weightUpdated(PredGraph<N, E> pv) {
        //do nothing
    }

    public Graph<N, E> getGraph() {
        return graph;
    }

    public InternalNode<N, E> getOrigin() {
        return origin;
    }

    public Relaxer<N, E> getRelaxer() {
        return relaxer;
    }

    public int getWeightIndex() {
        return weightIndex;
    }

    public ContextualReachability<N, E, Traversal<N, E>> getReachability() {
        return reachability;
    }
}