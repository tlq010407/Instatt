package com.example.instatt;

import org.junit.Test;
import static org.junit.Assert.*;

import com.example.instatt.Class.ClassType;

public class ClassTypeTest {

    @Test
    public void testConstructorAndGetters() {
        int testIconResourceId = 123; // Example resource ID
        ClassType testClassType = new ClassType("TestType", testIconResourceId);

        assertEquals("TestType", testClassType.getTypeName());
        assertEquals(testIconResourceId, testClassType.getIconResourceId());
    }

    @Test
    public void testGetTypeFromString() {
        assertEquals(ClassType.LECTURE, ClassType.getTypeFromString("LECTURE"));
        assertEquals(ClassType.COMPUTING, ClassType.getTypeFromString("COMPUTING"));
        assertEquals(ClassType.TUTORIAL, ClassType.getTypeFromString("TUTORIAL"));
        assertEquals(ClassType.LAB, ClassType.getTypeFromString("LAB"));
        assertNull(ClassType.getTypeFromString("UNKNOWN"));
    }
}

