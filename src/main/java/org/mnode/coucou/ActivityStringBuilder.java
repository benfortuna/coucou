/**
 * This file is part of Coucou.
 *
 * Copyright (c) 2011, Ben Fortuna [fortuna@micronode.com]
 *
 * Coucou is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Coucou is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Coucou.  If not, see <http://www.gnu.org/licenses/>.
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
//        .append(author).append("</td></tr>")
        .append(author).append("</td>")
        .append("<td style='font-size:1em;font-style:italic;color:silver;text-align:right'>")
        .append(timeFormatter.format(time)).append("</td></tr>")
        .append("<tr><td colspan='2' style='font-size:1em;text-align:left'>")
        .append(subject).append("</td></tr></table></html>");
//        .append("<tr><td style='font-size:1em;font-style:italic;color:silver;text-align:left'>")
//        .append(timeFormatter.format(time)).append("</td></tr></table></html>");
    
        return b.toString();
    }
}
