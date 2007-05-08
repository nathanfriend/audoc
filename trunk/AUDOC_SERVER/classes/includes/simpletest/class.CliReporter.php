<?php

class CliReporter extends SimpleReporter {

    var $faildetail_separator = "->";

    function CLIReporter($faildetail_separator = NULL) {
        $this->SimpleReporter();
        if (! is_null($faildetail_separator)) {
            $this->setFailDetailSeparator($faildetail_separator);
        }
    }

    /**
     * Print msg at the start of each test case
     */
    function paintCaseStart($test_name) {
        echo "Test case: $test_name\n";
    }

    function setFailDetailSeparator($separator) {
        $this->faildetail_separator = $separator;
    }

    /**
     * Return a formatted faildetail for printing.
     */
    function &_paintTestFailDetail(&$message) {
        $buffer = '';
        $faildetail = $this->getTestList();
        array_shift($faildetail);
        $buffer .= implode($this->faildetail_separator, $faildetail);
        $buffer .= $this->faildetail_separator . "$message\n";
        return $buffer;
    }

    /**
     * Paint fail faildetail to STDERR.
     */
    function paintFail($message) {
        parent::paintFail($message);
        echo 'FAIL' . $this->faildetail_separator . $this->_paintTestFailDetail($message);
    }

    /**
     * Paint exception faildetail to STDERR.
     */
    function paintException($message) {
        parent::paintException($message);
        echo 'EXCEPTION' . $this->faildetail_separator . $this->_paintTestFailDetail($message);
    }

    /**
     * Paint a footer with test case name, timestamp, counts of fails and
     * exceptions.
     */
    function paintFooter($test_name) {
        $buffer = $this->getTestCaseProgress() . '/' .
            $this->getTestCaseCount() . ' test cases complete: ';

        if (0 < ($this->getFailCount() + $this->getExceptionCount())) {
            $buffer .= $this->getPassCount() . " passes";
            if (0 < $this->getFailCount()) {
                $buffer .= ", " . $this->getFailCount() . " fails";
            }
            if (0 < $this->getExceptionCount()) {
                $buffer .= ", " . $this->getExceptionCount() . " exceptions";
            }
            $buffer .= ".\n";
            echo $buffer;
            exit(1);
        } else {
            echo $buffer . $this->getPassCount() . " passes.\n";
        }
    }
}

?>