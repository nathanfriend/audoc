<?php
    // $Id: shell_tester_test.php,v 1.1 2006/04/10 11:21:05 jonm Exp $

    Mock::generate('SimpleShell');
    
    class TestOfShellTestCase extends ShellTestCase {
        var $_mock_shell;
        
        function TestOfShellTestCase() {
            $this->ShellTestCase();
            $this->_mock_shell = false;
        }
        function &_getShell() {
            return $this->_mock_shell;
        }
        function testExitCode() {
            $this->_mock_shell = &new MockSimpleShell($this);
            $this->_mock_shell->setReturnValue('execute', 0);
            $this->_mock_shell->expectOnce('execute', array('ls'));
            $this->assertTrue($this->execute('ls'));
            $this->assertExitCode(0);
            $this->_mock_shell->tally();
        }
        function testOutput() {
            $this->_mock_shell = &new MockSimpleShell($this);
            $this->_mock_shell->setReturnValue('execute', 0);
            $this->_mock_shell->setReturnValue('getOutput', "Line 1\nLine 2\n");
            $this->assertOutput("Line 1\nLine 2\n");
        }
        function testOutputPatterns() {
            $this->_mock_shell = &new MockSimpleShell($this);
            $this->_mock_shell->setReturnValue('execute', 0);
            $this->_mock_shell->setReturnValue('getOutput', "Line 1\nLine 2\n");
            $this->assertOutputPattern('/line/i');
            $this->assertNoOutputPattern('/line 2/');
        }
    }
?>