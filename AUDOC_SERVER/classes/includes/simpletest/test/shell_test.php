<?php
    // $Id: shell_test.php,v 1.1 2006/04/10 11:21:07 jonm Exp $
    
    require_once(dirname(__FILE__) . '/../shell_tester.php');
    
    class TestOfShell extends UnitTestCase {
        function TestOfShell() {
            $this->UnitTestCase();
        }
        
        function testEcho() {
            $shell = &new SimpleShell();
            $this->assertIdentical($shell->execute('echo Hello'), 0);
            $this->assertWantedPattern('/Hello/', $shell->getOutput());
        }
        
        function testBadCommand() {
            $shell = &new SimpleShell();
            $this->assertNotEqual($ret = $shell->execute('blurgh! 2>&1'), 0);
        }
    }
    
    class TestOfShellTesterAndShell extends ShellTestCase {
        function TestOfShellTesterAndShell() {
            $this->ShellTestCase();
        }
        
        function testEcho() {
            $this->assertTrue($this->execute('echo Hello'));
            $this->assertExitCode(0);
            $this->assertoutput('Hello');
        }
        
        function testFileExistence() {
            $this->assertFileExists(dirname(__FILE__) . '/all_tests.php');
            $this->assertFileNotExists('wibble');
        }
        
        function testFilePatterns() {
            $this->assertFilePattern('/all_tests/i', dirname(__FILE__) . '/all_tests.php');
            $this->assertNoFilePattern('/sputnik/i', dirname(__FILE__) . '/all_tests.php');
        }
    }
?>