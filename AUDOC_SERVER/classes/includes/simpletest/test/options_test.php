<?php
    // $Id: options_test.php,v 1.1 2006/04/10 11:21:03 jonm Exp $
    
    require_once(dirname(__FILE__) . '/../options.php');
    
    class TestOfOptions extends UnitTestCase {
        function TestOfOptions() {
            $this->UnitTestCase();
        }
        
        function testMockBase() {
            $old_class = SimpleTestOptions::getMockBaseClass();
            SimpleTestOptions::setMockBaseClass('Fred');
            $this->assertEqual(SimpleTestOptions::getMockBaseClass(), 'Fred');
            SimpleTestOptions::setMockBaseClass($old_class);
        }
        
        function testStubBase() {
            $old_class = SimpleTestOptions::getStubBaseClass();
            SimpleTestOptions::setStubBaseClass('Fred');
            $this->assertEqual(SimpleTestOptions::getStubBaseClass(), 'Fred');
            SimpleTestOptions::setStubBaseClass($old_class);
        }
        
        function testIgnoreList() {
            $this->assertFalse(SimpleTestOptions::isIgnored('ImaginaryTestCase'));
            SimpleTestOptions::ignore('ImaginaryTestCase');
            $this->assertTrue(SimpleTestOptions::isIgnored('ImaginaryTestCase'));
        }
    }
    
    class RandomCompatibilityClass {
    }
    
    class RandomCompatibilitySubclass extends RandomCompatibilityClass {
    }
    
    class TestOfCompatibility extends UnitTestCase {
        function TestOfCompatibility() {
            $this->UnitTestCase();
        }
        
        function testIsA() {
            $this->assertTrue(SimpleTestCompatibility::isA(
                    new RandomCompatibilityClass(),
                    'RandomCompatibilityClass'));
            $this->assertFalse(SimpleTestCompatibility::isA(
                    new RandomCompatibilityClass(),
                    'RandomCompatibilitySubclass'));
            $this->assertTrue(SimpleTestCompatibility::isA(
                    new RandomCompatibilitySubclass(),
                    'RandomCompatibilityClass'));
        }
        
        function testIdentityOfObjects() {
            $object1 = new RandomCompatibilityClass();
            $object2 = new RandomCompatibilityClass();
            $this->assertIdentical($object1, $object2);
        }
    }
?>