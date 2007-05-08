<?php
/**
 * This file provides function to attempt to 
 * identify the mime type of a file from its extension
 *
 * PHP version 5
 *
 * @author     Jon Moss <jon.moss@audata.co.uk>
 * @copyright  2005 Audata Limited
 * @license    
 * @version    CVS: $Id: class.Mime.php,v 1.1 2007/03/26 08:13:43 jonm Exp $
 */

class Mime {
	
	private $types = array();

	public function Mime(){
		$this->popTypes();
	}
	
	public function getTypeFromName($filename) {
		$filename = basename($filename);
      	$filename = explode('.', $filename);
      	$ext = $filename[count($filename)-1];    	
	  	return $this->getTypeFromExt($ext);
	}
	
	public function getTypeFromExt($ext){
		if(isset($this->types[$ext])){
		  	return $this->types[$ext];
		  }else{
		  	return "application/octet-stream";
		  }
	}
	
	private function popTypes(){
		$this->types["ez"] = "application/andrew-inset";
		$this->types["hqx"] = "application/mac-binhex40";
		$this->types["cpt"] = "application/mac-compactpro";
		$this->types["doc"] = "application/msword";
		$this->types["bin"] = "application/octet-stream";
		$this->types["dms"] = "application/octet-stream";
		$this->types["lha"] = "application/octet-stream";
		$this->types["lzh"] = "application/octet-stream";
		$this->types["exe"] = "application/octet-stream";
		$this->types["class"] = "application/octet-stream";
		$this->types["so"] = "application/octet-stream";
		$this->types["dll"] = "application/octet-stream";
		$this->types["oda"] = "application/oda";
		$this->types["pdf"] = "application/pdf";
		$this->types["ai"] = "application/postscript";
		$this->types["eps"] = "application/postscript";
		$this->types["ps"] = "application/postscript";
		$this->types["smi"] = "application/smil";
		$this->types["smil"] = "application/smil";
		$this->types["wbxml"] = "application/vnd.wap.wbxml";
		$this->types["wmlc"] = "application/vnd.wap.wmlc";
		$this->types["wmlsc"] = "application/vnd.wap.wmlscriptc";
		$this->types["bcpio"] = "application/x-bcpio";
		$this->types["vcd"] = "application/x-cdlink";
		$this->types["pgn"] = "application/x-chess-pgn";
		$this->types["cpio"] = "application/x-cpio";
		$this->types["csh"] = "application/x-csh";
		$this->types["dcr"] = "application/x-director";
		$this->types["dir"] = "application/x-director";
		$this->types["dxr"] = "application/x-director";
		$this->types["dvi"] = "application/x-dvi";
		$this->types["spl"] = "application/x-futuresplash";
		$this->types["gtar"] = "application/x-gtar";
		$this->types["hdf"] = "application/x-hdf";
		$this->types["js"] = "application/x-javascript";
		$this->types["skp"] = "application/x-koan";
		$this->types["skd"] = "application/x-koan";
		$this->types["skt"] = "application/x-koan";
		$this->types["skm"] = "application/x-koan";
		$this->types["latex"] = "application/x-latex";
		$this->types["nc"] = "application/x-netcdf";
		$this->types["cdf"] = "application/x-netcdf";
		$this->types["sh"] = "application/x-sh";
		$this->types["shar"] = "application/x-shar";
		$this->types["swf"] = "application/x-shockwave-flash";
		$this->types["sit"] = "application/x-stuffit";
		$this->types["sv4cpio"] = "application/x-sv4cpio";
		$this->types["sv4crc"] = "application/x-sv4crc";
		$this->types["tar"] = "application/x-tar";
		$this->types["tcl"] = "application/x-tcl";
		$this->types["tex"] = "application/x-tex";
		$this->types["texinfo"] = "application/x-texinfo";
		$this->types["texi"] = "application/x-texinfo";
		$this->types["t"] = "application/x-troff";
		$this->types["tr"] = "application/x-troff";
		$this->types["roff"] = "application/x-troff";
		$this->types["man"] = "application/x-troff-man";
		$this->types["me"] = "application/x-troff-me";
		$this->types["ms"] = "application/x-troff-ms";
		$this->types["ustar"] = "application/x-ustar";
		$this->types["src"] = "application/x-wais-source";
		$this->types["xhtml"] = "application/xhtml+xml";
		$this->types["xht"] = "application/xhtml+xml";
		$this->types["zip"] = "application/zip";
		$this->types["au"] = "audio/basic";
		$this->types["snd"] = "audio/basic";
		$this->types["mid"] = "audio/midi";
		$this->types["midi"] = "audio/midi";
		$this->types["kar"] = "audio/midi";
		$this->types["mpga"] = "audio/mpeg";
		$this->types["mp2"] = "audio/mpeg";
		$this->types["mp3"] = "audio/mpeg";
		$this->types["aif"] = "audio/x-aiff";
		$this->types["aiff"] = "audio/x-aiff";
		$this->types["aifc"] = "audio/x-aiff";
		$this->types["m3u"] = "audio/x-mpegurl";
		$this->types["ram"] = "audio/x-pn-realaudio";
		$this->types["rm"] = "audio/x-pn-realaudio";
		$this->types["rpm"] = "audio/x-pn-realaudio-plugin";
		$this->types["ra"] = "audio/x-realaudio";
		$this->types["wav"] = "audio/x-wav";
		$this->types["pdb"] = "chemical/x-pdb";
		$this->types["xyz"] = "chemical/x-xyz";
		$this->types["bmp"] = "image/bmp";
		$this->types["gif"] = "image/gif";
		$this->types["ief"] = "image/ief";
		$this->types["jpeg"] = "image/jpeg";
		$this->types["jpg"] = "image/jpeg";
		$this->types["jpe"] = "image/jpeg";
		$this->types["png"] = "image/png";
		$this->types["tiff"] = "image/tiff";
		$this->types["tif"] = "image/tif";
		$this->types["djvu"] = "image/vnd.djvu";
		$this->types["djv"] = "image/vnd.djvu";
		$this->types["wbmp"] = "image/vnd.wap.wbmp";
		$this->types["ras"] = "image/x-cmu-raster";
		$this->types["pnm"] = "image/x-portable-anymap";
		$this->types["pbm"] = "image/x-portable-bitmap";
		$this->types["pgm"] = "image/x-portable-graymap";
		$this->types["ppm"] = "image/x-portable-pixmap";
		$this->types["rgb"] = "image/x-rgb";
		$this->types["xbm"] = "image/x-xbitmap";
		$this->types["xpm"] = "image/x-xpixmap";
		$this->types["xwd"] = "image/x-windowdump";
		$this->types["igs"] = "model/iges";
		$this->types["iges"] = "model/iges";
		$this->types["msh"] = "model/mesh";
		$this->types["mesh"] = "model/mesh";
		$this->types["silo"] = "model/mesh";
		$this->types["wrl"] = "model/vrml";
		$this->types["vrml"] = "model/vrml";
		$this->types["css"] = "text/css";
		$this->types["html"] = "text/html";
		$this->types["htm"] = "text/html";
		$this->types["asc"] = "text/plain";
		$this->types["txt"] = "text/plain";
		$this->types["rtx"] = "text/richtext";
		$this->types["rtf"] = "text/rtf";
		$this->types["sgml"] = "text/sgml";
		$this->types["sgm"] = "text/sgml";
		$this->types["tsv"] = "text/tab-seperated-values";
		$this->types["wml"] = "text/vnd.wap.wml";
		$this->types["wmls"] = "text/vnd.wap.wmlscript";
		$this->types["etx"] = "text/x-setext";
		$this->types["xml"] = "text/xml";
		$this->types["xsl"] = "text/xml";
		$this->types["mpeg"] = "video/mpeg";
		$this->types["mpg"] = "video/mpeg";
		$this->types["mpe"] = "video/mpeg";
		$this->types["qt"] = "video/quicktime";
		$this->types["mov"] = "video/quicktime";
		$this->types["mxu"] = "video/vnd.mpegurl";
		$this->types["avi"] = "video/x-msvideo";
		$this->types["movie"] = "video/x-sgi-movie";
		$this->types["ice"] = "x-conference-xcooltalk";
	}
}
?>