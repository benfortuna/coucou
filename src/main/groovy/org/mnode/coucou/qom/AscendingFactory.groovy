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

import javax.jcr.query.QueryManagerimport javax.jcr.query.qom.Orderingimport javax.jcr.query.qom.DynamicOperand

/**
 * @author Ben
 *
 */
public class AscendingFactory extends AbstractQomFactory {
     
     public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
         Ordering ordering
         if (FactoryBuilderSupport.checkValueIsTypeNotString(value, name, Ordering.class)) {
             ordering = (Ordering) value
         }
         else {
             DynamicOperand operand = attributes.remove('operand')
             ordering = queryManager.qomFactory.ascending(operand)
         }
         return ordering
     }
}
