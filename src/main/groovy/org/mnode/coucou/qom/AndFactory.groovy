/**
 * This file is part of Coucou.
 *
 * Copyright (c) 2010, Ben Fortuna [fortuna@micronode.com]
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
package org.mnode.coucou.qom

import javax.jcr.query.qom.Andimport javax.jcr.query.qom.Constraintimport javax.jcr.query.QueryManager

/**
 * @author Ben
 *
 */
public class AndFactory extends AbstractQomFactory {
     
     public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
         And constraint
         if (FactoryBuilderSupport.checkValueIsTypeNotString(value, name, And.class)) {
             constraint = (And) value
         }
         else {
             Constraint constraint1 = attributes.remove('constraint1')
             Constraint constraint2 = attributes.remove('constraint2')
             constraint = queryManager.qomFactory.and(constraint1, constraint2)
         }
         return constraint
     }
}
