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
 * Qmino bvba - Romeinsestraat 18 - 3001 Heverlee  (http://www.qmino.com)
 * Geovise bvba - Generaal Eisenhowerlei 9 - 2140 Antwerpen (http://www.geovise.com)
 */

package org.geolatte.graph.algorithms;

import org.geolatte.graph.EdgeLabel;
import org.geolatte.graph.Nodal;
import org.geolatte.graph.PredGraph;

/**
 * Encapsulates the relaxation step of a shortest path algorithm.
 * <p/>
 * From "Introduction to Algorithms 3rd edition":
 * The process of relaxing an edge (u, v) consists of testing whether we can improve the shortest path to v found so far
 * by going through u and, if so, updating (decreasing) the new total weight of v and its predecessor node
 *
 * @author Karel Maesen
 * @author Bert Vanhooff
 *
 * @param <N> The type of node.
 * @param <E> The type of edge label
 * @param <M> The type of modus
 */
interface Relaxer<N extends Nodal, E extends EdgeLabel<M>, M> {

    /**
     * Relaxes the edge from node <code>u</code> to node <code>v</node>. Both are given by their predecessor graphs.
     *
     * @param u     Predecessor graph representing current shortest path to node u.
     * @param v     Predecessor graph representing current shortest path to node v.
     * @param label The edge label that determines the weight of the edge between u and v.
     * @param modus The mode in which to execute the relaxation.
     * @return True if the weight of v was updated, false otherwise
     */
    public boolean relax(PredGraph<N> u, PredGraph<N> v, E label, M modus);

    /**
     * Returns the new total weight of the path to node v after relaxation has occured. Is only meaningful after
     * {@link #relax(PredGraph, PredGraph, org.geolatte.graph.EdgeLabel, Object)}
     * has been called.
     * 
     * @return The new total weight of the path to node v.
     */
    public float newTotalWeight();

}
