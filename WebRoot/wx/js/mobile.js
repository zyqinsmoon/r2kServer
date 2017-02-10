function getOsVersion()
{
  var userOS;    // will either be iOS, Android or unknown
  var userOSver; // this is a string, use Number(userOSver) to convert
  var ua = navigator.userAgent;
  var uaindex;
  // determine OS
  if ( ua.match(/iPad/i) || ua.match(/iPhone/i) )
  {
    userOS = 'iOS';
    uaindex = ua.indexOf( 'OS ' );
  }
  else if ( ua.match(/Android/i) )
  {
    userOS = 'Android';
    uaindex = ua.indexOf( 'Android ' );
  }
  else
  {
    userOS = 'unknown';
  }
  // determine version
  if ( userOS === 'iOS'  &&  uaindex > -1 )
  {
    userOSver = ua.substr( uaindex + 3, 3 ).replace( '_', '.' );
  }
  else if ( userOS === 'Android'  &&  uaindex > -1 )
  {
    userOSver = ua.substr( uaindex + 8, 3 );
  }
  else
  {
    userOSver = 'unknown';
  }
  
return userOS;

}
function isMicroMessenger(){
	var ua = navigator.userAgent;
	if ( ua.match(/MicroMessenger/i) ){
	    return true;
	}
	return false;	
}