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

import java.util.Map;

import org.jdivisitor.debugger.Debugger;
import org.jdivisitor.debugger.launcher.LocalVMLauncher;
import org.jdivisitor.debugger.launcher.VMConnector;

import com.sun.jdi.VirtualMachine;

public final class MethodProfiler {

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.err.println("Incorrect number of command-line arguments");
            System.err.println("Usage: MethodProfiler <main-class> <options>");

            System.exit(1);
        }

        MethodProfilerRequests requests = new MethodProfilerRequests();
        MethodProfilerVisitor visitor = new MethodProfilerVisitor();

        VMConnector connector = new LocalVMLauncher(args[0], args[1], null,
                null);
        VirtualMachine vm = connector.connect();

        Debugger debugger = new Debugger(vm);
        debugger.requestEvents(requests);
        debugger.run(visitor, 10 * 1000);   // Maximum runtime of 10 seconds

        printResults(visitor.getMethodCounts());
    }

    /**
     * Pretty print the results.
     *
     * @param methodTable Table containing the results
     */
    private static void printResults(Map<String, Integer> methodTable) {
        // Print the header
        System.out.format("%-70s %5s\n", "Method", "Count");
        for (int i = 0; i < (70 + 1 + 5); i++) {
            System.out.print("=");
        }
        System.out.println();

        // Print the results
        for (Map.Entry<String, Integer> entry : methodTable.entrySet()) {
            String method = entry.getKey();
            int count = entry.getValue();

            System.out.format("%-70s %5d\n", method, count);
        }
    }
}
