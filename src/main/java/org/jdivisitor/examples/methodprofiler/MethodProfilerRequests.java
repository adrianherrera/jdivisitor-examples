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

import com.sun.jdi.request.EventRequestManager;
import com.sun.jdi.request.MethodEntryRequest;

import org.jdivisitor.debugger.request.EventRequestor;

/**
 * Request the events we want delivered to the debugger. We are only interested
 * in method entry events.
 *
 * @author Adrian Herrera
 */
class MethodProfilerRequests implements EventRequestor {

    // Exclude these packages
    private final static String[] EXCLUDES = { "sun.*", "com.sun.*" };

    @Override
    public void requestEvents(EventRequestManager erm) {
        // Exclude some packages
        MethodEntryRequest mer = erm.createMethodEntryRequest();
        for (String exclude : EXCLUDES) {
            mer.addClassExclusionFilter(exclude);
        }
        mer.enable();
    }
}
