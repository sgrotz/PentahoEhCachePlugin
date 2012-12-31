package com.ehcache.pentaho;


/*
EhCache Pentaho Plugin
Author: Stephan Grotz (stephan.grotz@gmail.com)
URL: https://github.com/sgrotz/PentahoEhCachePlugin

Copyright (c) 2012 Stephan Grotz

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated 
documentation files (the "Software"), to deal in the Software without restriction, including without limitation 
the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and 
to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of 
the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO 
THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE 
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, 
TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

*/



import org.pentaho.di.i18n.BaseMessages;

public class Messages
{
	public static final Class<Messages> PKG = Messages.class;

	public static String getString(String key)
	{
		return BaseMessages.getString(PKG, key);
	}

	public static String getString(String key, String param1)
	{
		return BaseMessages.getString(PKG, key, param1);
	}

	public static String getString(String key, String param1, String param2)
	{
		return BaseMessages.getString(PKG, key, param1, param2);
	}

	public static String getString(String key, String param1, String param2, String param3)
	{
		return BaseMessages.getString(PKG, key, param1, param2, param3);
	}

	public static String getString(String key, String param1, String param2, String param3, String param4)
	{
		return BaseMessages.getString(PKG, key, param1, param2, param3, param4);
	}

	public static String getString(String key, String param1, String param2, String param3, String param4, String param5)
	{
		return BaseMessages.getString(PKG, key, param1, param2, param3, param4, param5);
	}

	public static String getString(String key, String param1, String param2, String param3, String param4, String param5, String param6)
	{
		return BaseMessages.getString(PKG, key, param1, param2, param3, param4, param5, param6);
	}
}
