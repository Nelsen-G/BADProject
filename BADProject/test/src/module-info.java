module Proj {
	opens main;
	opens model;
//	opens util;
	opens assets;
	
	requires javafx.graphics;
	requires javafx.controls;
	requires jfxtras.labs;
	requires java.sql;
}