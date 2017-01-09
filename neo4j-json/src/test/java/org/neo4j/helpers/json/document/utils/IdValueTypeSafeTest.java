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

import java.math.BigInteger;

import org.junit.Assert;
import org.junit.Test;


/**
 * @author Omar Rampado
 *
 */
public class IdValueTypeSafeTest {

	IdValueTypeSafe value;
	

	/**
	 * Test method for {@link org.neo4j.helpers.json.document.utils.IdValueTypeSafe#IdValueTypeSafe(java.lang.Object)}.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testIdValueTypeSafe() {
		value = new IdValueTypeSafe(null);
	}

	/**
	 * Test method for {@link org.neo4j.helpers.json.document.utils.IdValueTypeSafe#toCypher()}.
	 */
	@Test
	public void testToCypherString() {
		value = new IdValueTypeSafe("1");
		Assert.assertEquals("'1'", value.toCypher());
	}

	@Test
	public void testToCypherLong() {
		value = new IdValueTypeSafe(1L);
		Assert.assertEquals("1", value.toCypher());
	}
	
	@Test
	public void testToCypherNumber() {
		value = new IdValueTypeSafe(new BigInteger("1"));
		Assert.assertEquals("1", value.toCypher());
	}
	
	/**
	 * Test method for {@link org.neo4j.helpers.json.document.utils.IdValueTypeSafe#isString()}.
	 */
	@Test
	public void testIsStringString() {
		value = new IdValueTypeSafe("1");
		Assert.assertTrue(value.isString());
	}

	@Test
	public void testIsStringLong() {
		value = new IdValueTypeSafe(1L);
		Assert.assertFalse(value.isString());
	}
	
	@Test
	public void testIsStringNumber() {
		value = new IdValueTypeSafe(new BigInteger("1"));
		Assert.assertFalse(value.isString());
	}
	
	/**
	 * Test method for {@link org.neo4j.helpers.json.document.utils.IdValueTypeSafe#isNumber()}.
	 */
	@Test
	public void testIsNumberString() {
		value = new IdValueTypeSafe("1");
		Assert.assertFalse(value.isNumber());
	}

	@Test
	public void testIsNumberLong() {
		value = new IdValueTypeSafe(1L);
		Assert.assertTrue(value.isNumber());
	}

	@Test
	public void testIsNumberNumber() {
		value = new IdValueTypeSafe(new BigInteger("1"));
		Assert.assertTrue(value.isNumber());
	}
	
	/**
	 * Test method for {@link org.neo4j.helpers.json.document.utils.IdValueTypeSafe#getValue()}.
	 */
	@Test
	public void testGetValueString() {
		value = new IdValueTypeSafe("1");
		Assert.assertEquals("1", value.getValue());
	}

	@Test
	public void testGetValueLong() {
		value = new IdValueTypeSafe(1L);
		Assert.assertEquals(1L, value.getValue());
	}

	@Test
	public void testGetValueNumber() {
		value = new IdValueTypeSafe(new BigInteger("1"));
		Assert.assertEquals(new BigInteger("1"), value.getValue());
	}
	
	/**
	 * Test method for {@link org.neo4j.helpers.json.document.utils.IdValueTypeSafe#toString()}.
	 */
	@Test
	public void testToStringString() {
		value = new IdValueTypeSafe("1");
		Assert.assertEquals("1", value.toString());
	}

	@Test
	public void testToStringLong() {
		value = new IdValueTypeSafe(1L);
		Assert.assertEquals("1", value.toString());
	}
	
	@Test
	public void testToStringNumber() {
		value = new IdValueTypeSafe(new BigInteger("1"));
		Assert.assertEquals("1",value.toString());
	}
	

}
