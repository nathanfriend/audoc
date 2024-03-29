<?php
    // $Id: real_sites_test.php,v 1.1 2006/04/10 11:21:03 jonm Exp $
    
    require_once(dirname(__FILE__) . '/../web_tester.php');

    class LiveSitesTestCase extends WebTestCase {
        function LiveSitesTestCase() {
            $this->WebTestCase();
        }
        
        function testLastCraft() {
            $this->assertTrue($this->get('http://www.lastcraft.com'));
            $this->assertResponse(array(200));
            $this->assertMime(array('text/html'));
            $this->clickLink('About');
            $this->assertTitle('About Last Craft');
        }
        
        function testSourceforge() {
            $this->assertTrue($this->get('http://sourceforge.net/'));
            $this->setField('words', 'simpletest');
            $this->assertTrue($this->clickImageByName('imageField'));
            $this->assertTitle('SourceForge.net: Search');
            $this->assertTrue($this->clickLink('<span style="background-color:pink">SimpleTest</span>'));
            $this->clickLink('statistics');
            $this->assertWantedPattern('/Statistics for the past 7 days/');
            $this->assertTrue($this->setField('report', 'Monthly'));
            $this->clickSubmit('Change Stats View');
            $this->assertWantedPattern('/Statistics for the past \d+ months/');
        }
        
        function testPhpLondon() {
            $this->get('http://www.phplondon.org/');
            $this->clickLink('Wiki is here');
            $this->clickLink('Members');
            $this->clickLink('MarcusBaker');
            $this->clickLink('SimpleTest');
            $this->assertTitle('PhpLondonWiki : SimpleTest');
            $this->back();
            $this->back();
            $this->assertTitle('PhpLondonWiki : Members');
        }
    }
?>