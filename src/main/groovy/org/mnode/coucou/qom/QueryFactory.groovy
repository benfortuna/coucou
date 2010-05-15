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
package org.mnode.coucou.qom

import javax.jcr.query.QueryManagerimport javax.jcr.query.Queryimport javax.jcr.query.qom.Sourceimport javax.jcr.query.qom.Constraintimport javax.jcr.query.qom.Orderingimport javax.jcr.query.qom.Column

/**
 * @author Ben
 *
 */
public class QueryFactory extends AbstractQomFactory {
     
     public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
         Query query
         if (FactoryBuilderSupport.checkValueIsTypeNotString(value, name, Query.class)) {
             query = (Query) value
         }
         else {
             Source source = attributes.remove('source')
             Constraint constraint = attributes.remove('constraint')
             Ordering[] orderings = attributes.remove('orderings')
             Column[] columns = attributes.remove('columns')
             query = queryManager.qomFactory.createQuery(source, constraint, orderings, columns)
         }
         return query
     }
}
