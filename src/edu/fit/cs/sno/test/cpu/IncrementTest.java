package edu.fit.cs.sno.test.cpu;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import edu.fit.cs.sno.snes.Core;
import edu.fit.cs.sno.snes.common.Size;
import edu.fit.cs.sno.snes.cpu.CPU;
import edu.fit.cs.sno.snes.cpu.instructions.Increment;
import edu.fit.cs.sno.snes.mem.LoROMMemory;
import edu.fit.cs.sno.util.Settings;

/**
 * Increment CPU Instruction tests
 */
public class IncrementTest extends TestCase {

	@Before
	public void setUp() {
		new Increment();// For 100% coverage
		Settings.init();
		CPU.resetCPU();
		Core.mem = new LoROMMemory();
	}
	
	@Test 
	public void testIncrementAccumulator() {
		// Test operation
		CPU.a.setValue(0);
		CPU.doOp(0x1A, null);
		assertEquals(CPU.a.getValue(), 1);
		
		// Test Zero Flag
		CPU.a.setValue(0xFFFF);
		CPU.doOp(0x1A, null);
		assertEquals(CPU.a.getValue(), 0);
		assertTrue(CPU.status.isZero());
		
		// Test Negative Flag
		CPU.a.setValue(0x7FFF);
		CPU.doOp(0x1A, null);
		assertEquals(CPU.a.getValue(), 0x8000);
		assertTrue(CPU.status.isNegative());
	}
	
	@Test 
	public void testIncrementDirectPage() {
		// 8-bit memory access
		CPU.status.setMemoryAccess(true);
		
		// Test operation
		Core.mem.set(Size.BYTE, 0, 26, 0);
		CPU.dp.setValue(4);
		CPU.doOp(0xE6, new int[] {22});
		assertEquals(Core.mem.get(Size.BYTE, 0, 26), 1);
		
		// Test Zero Flag
		Core.mem.set(Size.BYTE, 0, 28, 0xFF);
		CPU.dp.setValue(6);
		CPU.doOp(0xE6, new int[] {22});
		assertEquals(Core.mem.get(Size.BYTE, 0, 28), 0);
		assertTrue(CPU.status.isZero());
		
		// Test Negative Flag
		Core.mem.set(Size.BYTE, 0, 22, 0x7F);
		CPU.dp.setValue(0);
		CPU.doOp(0xE6, new int[] {22});
		assertEquals(Core.mem.get(Size.BYTE, 0, 22), 0x80);
		assertTrue(CPU.status.isNegative());
		
		// 16-bit memory access
		CPU.status.setMemoryAccess(false);
		
		// Test 16-bit
		Core.mem.set(Size.SHORT, 0, 26, 0x7EF0);
		CPU.dp.setValue(4);
		CPU.doOp(0xE6, new int[] {22});
		assertEquals(Core.mem.get(Size.SHORT, 0, 26), 0x7EF1);
	}
	
	@Test 
	public void testIncrementAbsolute() {
		// 8-bit memory access
		CPU.status.setMemoryAccess(true);
		
		// Test operation
		Core.mem.set(Size.BYTE, 0x7E, 0x2112, 0);
		CPU.dbr.setValue(0x7E);
		CPU.doOp(0xEE, new int[] {0x12, 0x21});
		assertEquals(Core.mem.get(Size.BYTE, 0x7E, 0x2112), 1);
		
		// Test Zero Flag
		Core.mem.set(Size.BYTE, 0x7E, 0x2112, 0xFF);
		CPU.dbr.setValue(0x7E);
		CPU.doOp(0xEE, new int[] {0x12, 0x21});
		assertEquals(Core.mem.get(Size.BYTE, 0x7E, 0x2112), 0);
		assertTrue(CPU.status.isZero());
		
		// Test Negative Flag
		Core.mem.set(Size.BYTE, 0x7E, 0x2112, 0x7F);
		CPU.dbr.setValue(0x7E);
		CPU.doOp(0xEE, new int[] {0x12, 0x21});
		assertEquals(Core.mem.get(Size.BYTE, 0x7E, 0x2112), 0x80);
		assertTrue(CPU.status.isNegative());
		
		// 16-bit memory access
		CPU.status.setMemoryAccess(false);
		
		// Test 16-bit
		Core.mem.set(Size.SHORT, 0x7E, 0x2112, 0x7EF0);
		CPU.dbr.setValue(0x7E);
		CPU.doOp(0xEE, new int[] {0x12, 0x21});
		assertEquals(Core.mem.get(Size.SHORT, 0x7E, 0x2112), 0x7EF1);
	}
	
	@Test 
	public void testIncrementDirectPageX() {
		// 8-bit memory access
		CPU.status.setMemoryAccess(true);
		
		// Test operation
		Core.mem.set(Size.BYTE, 0, 28, 0);
		CPU.dp.setValue(4);
		CPU.x.setValue(2);
		CPU.doOp(0xF6, new int[] {22});
		assertEquals(Core.mem.get(Size.BYTE, 0, 28), 1);
		
		// Test Zero Flag
		Core.mem.set(Size.BYTE, 0, 32, 0xFF);
		CPU.dp.setValue(6);
		CPU.x.setValue(4);
		CPU.doOp(0xF6, new int[] {22});
		assertEquals(Core.mem.get(Size.BYTE, 0, 32), 0);
		assertTrue(CPU.status.isZero());
		
		// Test Negative Flag
		Core.mem.set(Size.BYTE, 0, 30, 0x7F);
		CPU.dp.setValue(0);
		CPU.x.setValue(8);
		CPU.doOp(0xF6, new int[] {22});
		assertEquals(Core.mem.get(Size.BYTE, 0, 30), 0x80);
		assertTrue(CPU.status.isNegative());
		
		// 16-bit memory access
		CPU.status.setMemoryAccess(false);
		
		// Test 16-bit
		Core.mem.set(Size.SHORT, 0, 28, 0x7EF0);
		CPU.dp.setValue(4);
		CPU.x.setValue(2);
		CPU.doOp(0xF6, new int[] {22});
		assertEquals(Core.mem.get(Size.SHORT, 0, 28), 0x7EF1);
	}
	
	@Test 
	public void testIncrementAbsoluteX() {
		// 8-bit memory access
		CPU.status.setMemoryAccess(true);
		
		// Test operation
		Core.mem.set(Size.BYTE, 0x7E, 0x2121, 0);
		CPU.dbr.setValue(0x7E);
		CPU.x.setValue(0x0F);
		CPU.doOp(0xFE, new int[] {0x12, 0x21});
		assertEquals(Core.mem.get(Size.BYTE, 0x7E, 0x2121), 1);
		
		// Test Zero Flag
		Core.mem.set(Size.BYTE, 0x7E, 0x2121, 0xFF);
		CPU.dbr.setValue(0x7E);
		CPU.x.setValue(0x0F);
		CPU.doOp(0xFE, new int[] {0x12, 0x21});
		assertEquals(Core.mem.get(Size.BYTE, 0x7E, 0x2121), 0);
		assertTrue(CPU.status.isZero());
		
		// Test Negative Flag
		Core.mem.set(Size.BYTE, 0x7E, 0x2121, 0x7F);
		CPU.dbr.setValue(0x7E);
		CPU.x.setValue(0x0F);
		CPU.doOp(0xFE, new int[] {0x12, 0x21});
		assertEquals(Core.mem.get(Size.BYTE, 0x7E, 0x2121), 0x80);
		assertTrue(CPU.status.isNegative());
		
		// 16-bit memory access
		CPU.status.setMemoryAccess(false);
		
		// Test 16-bit
		Core.mem.set(Size.SHORT, 0x7E, 0x2116, 0x7EF0);
		CPU.dbr.setValue(0x7E);
		CPU.x.setValue(4);
		CPU.doOp(0xFE, new int[] {0x12, 0x21});
		assertEquals(Core.mem.get(Size.SHORT, 0x7E, 0x2116), 0x7EF1);
	}
	
	@Test 
	public void testIncrementX() {
		// Test operation
		CPU.x.setValue(0);
		CPU.doOp(0xE8, null);
		assertEquals(CPU.x.getValue(), 1);
		
		// Test Zero Flag
		CPU.x.setValue(0xFFFF);
		CPU.doOp(0xE8, null);
		assertEquals(CPU.x.getValue(), 0);
		assertTrue(CPU.status.isZero());
		
		// Test Negative Flag
		CPU.x.setValue(0x7FFF);
		CPU.doOp(0xE8, null);
		assertEquals(CPU.x.getValue(), 0x8000);
		assertTrue(CPU.status.isNegative());
	}
	
	@Test 
	public void testIncrementY() {
		// Test operation
		CPU.y.setValue(0);
		CPU.doOp(0xC8, null);
		assertEquals(CPU.y.getValue(), 1);
		
		// Test Zero Flag
		CPU.y.setValue(0xFFFF);
		CPU.doOp(0xC8, null);
		assertEquals(CPU.y.getValue(), 0);
		assertTrue(CPU.status.isZero());
		
		// Test Negative Flag
		CPU.y.setValue(0x7FFF);
		CPU.doOp(0xC8, null);
		assertEquals(CPU.y.getValue(), 0x8000);
		assertTrue(CPU.status.isNegative());
	}
	
}
