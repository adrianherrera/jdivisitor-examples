/*
 * JDIVisitor
 * Copyright (C) 2014  Adrian Herrera
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package org.jdivisitor.examples.methodprofiler;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.sun.jdi.event.MethodEntryEvent;

import org.jdivisitor.debugger.event.visitor.EmptyEventVisitor;

/**
 * Records the number of times a particular method is called.
 *
 * @author Adrian Herrera
 */
class MethodProfilerVisitor extends EmptyEventVisitor {

    /**
     * Map of method name to call count.
     */
    private final Map<String, Integer> methodCount;

    public MethodProfilerVisitor() {
        methodCount = new HashMap<String, Integer>();
    }

    @Override
    public void visit(MethodEntryEvent event) {
        String className = event.method().declaringType().name();
        String method = className + "." + event.method().name();
        int count;

        if (!methodCount.containsKey(method)) {
            count = 1;
        } else {
            count = methodCount.get(method) + 1;
        }

        methodCount.put(method, count);
    }

    /**
     * Gets a table of method names to call count. The table is sorted by call
     * count.
     *
     * @return Method name/call count table
     */
    public Map<String, Integer> getMethodCounts() {
        return sortMethodCounts();
    }

    /**
     * Sort the {@code methodCount} table.
     *
     * @return A sorted table
     */
    private Map<String, Integer> sortMethodCounts() {
        MethodCountComparator comparator = new MethodCountComparator(
                methodCount);
        Map<String, Integer> sortedMethodCount = new TreeMap<String, Integer>(
                comparator);

        sortedMethodCount.putAll(methodCount);

        return sortedMethodCount;
    }

    /**
     * Compares an entry in the method count table, allowing for the entries in
     * the table to be sorted in descending order.
     */
    private static class MethodCountComparator implements Comparator<String> {

        private final Map<String, Integer> map;

        public MethodCountComparator(Map<String, Integer> map) {
            this.map = map;
        }

        @Override
        public int compare(String o1, String o2) {
            if (map.get(o1) >= map.get(o2)) {
                return -1;
            } else {
                return 1;
            }
        }

    }
}
