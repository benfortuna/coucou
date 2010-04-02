/**
 * This file is part of Base Modules.
 *
 * Copyright (c) 2010, Ben Fortuna [fortuna@micronode.com]
 *
 * Base Modules is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Base Modules is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Base Modules.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.mnode.coucou;

import java.util.Date;

import com.ocpsoft.pretty.time.PrettyTime;

/**
 * @author Ben
 *
 */
public class ActivityStringBuilder {

    private final PrettyTime timeFormatter = new PrettyTime();
    
    private String author;
    
    private String subject;
    
    private Date time;
    
    public ActivityStringBuilder author(String author) {
        this.author = author;
        return this;
    }
    
    public ActivityStringBuilder subject(String subject) {
        this.subject = subject;
        return this;
    }
    
    public ActivityStringBuilder time(Date time) {
        this.time = time;
        return this;
    }
    
    public String build() {
        StringBuilder b = new StringBuilder().append("<html><table width=100%>")
        .append("<tr><td style='font-size:1em;font-weight:bold;color:silver;text-align:left'>")
        .append(author).append("</td></tr>")
        .append("<tr><td style='font-size:1em;text-align:left'>")
        .append(subject).append("</td></tr>")
        .append("<tr><td style='font-size:1em;font-style:italic;color:silver;text-align:left'>")
        .append(timeFormatter.format(time)).append("</td></tr></table></html>");
    
        return b.toString();
    }
}
