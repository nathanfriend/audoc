<?php
    /**
     *	adapter for SimpleTest to use PEAR PHPUnit test cases
     *	@package	SimpleTest
     *	@subpackage Extensions
     *	@version	$Id: pear_test_case.php,v 1.1 2006/04/10 11:21:49 jonm Exp $
     */
    
    /**#@+
     * include SimpleTest files
     */
    require_once dirname(__FILE__).DIRECTORY_SEPARATOR
    	.'..'.DIRECTORY_SEPARATOR .'unit_tester.php';
    require_once dirname(__FILE__).DIRECTORY_SEPARATOR
            .'..'.DIRECTORY_SEPARATOR .'expectation.php';
	/**#@-*/
   
    /**
     *    Adapter for PEAR PHPUnit test case to allow
     *    legacy PEAR test cases to be used with SimpleTest.
     *    @package      SimpleTest
     *    @subpackage   Extensions
     */
    class PHPUnit_TestCase extends SimpleTestCase {
        var $_loosely_typed;
        
        /**
         *    Constructor. Sets the test name.
         *    @param $label        Test name to display.
         *    @public
         */
        function PHPUnit_TestCase($label = false) {
            $this->SimpleTestCase($label);
            $this->_loosely_typed = false;
        }
        
        /**
         *    Will test straight equality if set to loose
         *    typing, or identity if not.
         *    @param $first          First value.
         *    @param $second         Comparison value.
         *    @param $message        Message to display.
         *    @public
         */
        function assertEquals($first, $second, $message = "%s", $delta = 0) {
            if ($this->_loosely_typed) {
                $expectation = &new EqualExpectation($first);
            } else {
                $expectation = &new IdenticalExpectation($first);
            }
            $this->assertExpectation($expectation, $second, $message);
        }
        
        /**
         *    Passes if the value tested is not null.
         *    @param $value          Value to test against.
         *    @param $message        Message to display.
         *    @public
         */
        function assertNotNull($value, $message = "%s") {
            parent::assertTrue(isset($value), $message);
        }
        
        /**
         *    Passes if the value tested is null.
         *    @param $value          Value to test against.
         *    @param $message        Message to display.
         *    @public
         */
        function assertNull($value, $message = "%s") {
            parent::assertTrue(!isset($value), $message);
        }
        
        /**
         *    In PHP5 the identity test tests for the same
         *    object. THis is a reference test in PHP4.
         *    @param $first          First object handle.
         *    @param $second         Hopefully the same handle.
         *    @param $message        Message to display.
         *    @public
         */
        function assertSame($first, $second, $message = "%s") {
            $this->assertExpectation(new IdenticalExpectation($first), $second, $message);
        }
        
        /**
         *    In PHP5 the identity test tests for the same
         *    object. THis is a reference test in PHP4.
         *    @param $first          First object handle.
         *    @param $second         Hopefully a different handle.
         *    @param $message        Message to display.
         *    @public
         */
        function assertNotSame($first, $second, $message = "%s") {
            $this->assertExpectation(new NotIdenticalExpectation($first), $second, $message);
        }
        
        /**
         *    Sends pass if the test condition resolves true,
         *    a fail otherwise.
         *    @param $condition      Condition to test true.
         *    @param $message        Message to display.
         *    @public
         */
        function assertTrue($condition, $message = "%s") {
            parent::assertTrue($condition, $message);
        }
        
        /**
         *    Sends pass if the test condition resolves false,
         *    a fail otherwise.
         *    @param $condition      Condition to test false.
         *    @param $message        Message to display.
         *    @public
         */
        function assertFalse($condition, $message = "%s") {
            parent::assertTrue(!$condition, $message);
        }
        
        /**
         *    Tests a regex match. Needs refactoring.
         *    @param $pattern        Regex to match.
         *    @param $subject        String to search in.
         *    @param $message        Message to display.
         *    @public
         */
        function assertRegExp($pattern, $subject, $message = "%s") {
            $this->assertExpectation(
                    new WantedPatternExpectation($pattern),
                    $subject,
                    $message);
        }
        
        /**
         *    Tests the type of a value.
         *    @param $value          Value to take type of.
         *    @param $type           Hoped for type.
         *    @param $message        Message to display.
         *    @public
         */
        function assertType($value, $type, $message = "%s") {
            parent::assertTrue(gettype($value) == strtolower($type), $message);
        }
        
        /**
         *    Sets equality operation to act as a simple equal
         *    comparison only, allowing a broader range of
         *    matches.
         *    @param $loosely_typed     True for broader comparison.
         *    @public
         */
        function setLooselyTyped($loosely_typed) {
            $this->_loosely_typed = $loosely_typed;
        }

        /**
         *    For progress indication during
         *    a test amongst other things.
         *    @return            Usually one.
         *    @public
         */
        function countTestCases() {
            return $this->getSize();
        }
        
        /**
         *    Accessor for name, normally just the class
         *    name.
         *    @public
         */
        function getName() {
            return $this->getLabel();
        }
        
        /**
         *    Does nothing. For compatibility only.
         *    @param $name        Dummy
         *    @public
         */
        function setName($name) {
        }
    }
?>
