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

import javax.jcr.query.QueryManagerimport javax.jcr.query.qom.Orderingimport javax.jcr.query.qom.DynamicOperandimport javax.jcr.query.qom.Comparisonimport javax.jcr.query.qom.StaticOperand

/**
 * @author Ben
 *
 */
public class ComparisonFactory extends AbstractQomFactory {
     
     public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
         Comparison comparison
         if (FactoryBuilderSupport.checkValueIsTypeNotString(value, name, Comparison.class)) {
             comparison = (Comparison) value
         }
         else {
             DynamicOperand operand1 = attributes.remove('operand1')
             String operator = attributes.remove('operator')
             def operand2Value = attributes.remove('operand2')
             StaticOperand operand2 = queryManager.qomFactory.literal(valueFactory.createValue(operand2Value))
             comparison = queryManager.qomFactory.comparison(operand1, operator, operand2)
         }
         return comparison
     }
}
