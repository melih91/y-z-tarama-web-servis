package com.Mlh;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

@WebService
@SOAPBinding(style = Style.RPC)
public interface callwebservice {

	public String my();

	public int checkUserImage(@WebParam(name = 	"tcNumber")String tc,  @WebParam(name= "ImageByte") byte[] imageBytes);

}
