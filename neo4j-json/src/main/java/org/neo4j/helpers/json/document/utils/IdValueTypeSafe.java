/**
 * Copyright (c) 2016 LARUS Business Automation [http://www.larus-ba.it]
 * <p>
 * This file is part of the "LARUS Integration Framework for Neo4j".
 * <p>
 * The "LARUS Integration Framework for Neo4j" is licensed
 * under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.neo4j.helpers.json.document.utils;

/**
 * Used to encapsulate the value of a id's field.
 * It's solve the problem:
 *  id: 1 <> id: '1' 
 * @author Omar Rampado
 *
 */
public class IdValueTypeSafe {

	private Object value;

	/**
	 * Wrap the real value
	 * @param value
	 */
	public IdValueTypeSafe(Object value) {
		if(value == null)
		{
			throw new IllegalArgumentException("value cannot be null");
		}
		this.value = value;
	}
	
	/**
	 * return a filter value according with type of real value
	 * @return
	 */
	public String toCypher(){
		if(isString())
		{
			return "'"+toString()+"'";
		}
		return toString();
	}
	
	public boolean isString(){
		return (value instanceof String);
	}
	
	public boolean isNumber(){
		return !isString();
	}
	
	/**
	 * get the original value
	 * @return
	 */
	public Object getValue(){
		return this.value;
	}

	@Override
	public String toString() {
		return value.toString();
	}
	
}
