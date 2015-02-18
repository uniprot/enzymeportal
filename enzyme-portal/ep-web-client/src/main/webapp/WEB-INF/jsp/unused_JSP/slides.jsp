<%-- 
    Document   : slides
    Created on : Jan 31, 2013, 1:49:58 PM
    Author     : joseph
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
      <title>jCarousel Demo</title>
      <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.3.2/jquery.min.js"></script>
      <script type="text/javascript" src="javascripts/lib/jquery.jcarousel.min.js"></script>
      <link rel="stylesheet" type="text/css" href="/enzymeportal/images/skins/default/skin2.css" />
<!--      <link rel="stylesheet" type="text/css" href="stylesheets/style.css" />-->

      <script type="text/javascript">
            /**
            * We use the initCallback callback
            * to assign functionality to the controls
            */
            function mycarousel_initCallback(carousel) {
                jQuery('.jcarousel-control a').bind('click', function () {
                    carousel.scroll(jQuery.jcarousel.intval(jQuery(this).text()));
                    return false; 
                });

                jQuery('#mycarousel-next').bind('click', function () {
                    carousel.next();
                    return false;
                });

                jQuery('#mycarousel-prev').bind('click', function () {
                    carousel.prev();
                    return false;
                });

                // Pause autoscrolling if the user moves with the cursor over the clip.
        	    carousel.clip.hover(function() {
        	        carousel.stopAuto();
        	    }, function() {
        	        carousel.startAuto();
        	    });

            };

            // Ride the carousel...
            jQuery(document).ready(function() {
              jQuery('#mycarousel').jcarousel({
              scroll: 1, animation:700, visible:1, auto:5, wrap:"both",
                    initCallback: mycarousel_initCallback,
                    itemVisibleInCallback: {
                        onAfterAnimation: function (c, o, i, s) {
                            i = (i - 1) % $('#mycarousel li').size();
                            jQuery('.jcarousel-control a').removeClass('active').addClass('inactive');
                            jQuery('.jcarousel-control a:eq(' + i + ')').removeClass('inactive').addClass('active');
                        }
                    }
          });  
      });
      </script>
   </head>
   <body>
      <div class="container">
         <ul id="mycarousel" class="jcarousel-skin-aqua">
            <li>
               <div>
                  <img src="http://culture.compulenta.ru/upload/iblock/0fc/iceberg-poster.jpg" />
                  <span>
                     <h4>Iceberg Dead Ahead!</h4>
                     Michelle, I don't regret this, but I both rue and lament it.<br/><br/>
                     And why did 'I' have to take a cab?
                     <a href="/" title="Link">Click this Link</a>
                  </span>
               </div>
            </li>
            <li>
               <div>
                  <img src="http://www.laleonaecolodge.com/tester/hola/userfiles/costa-rica-rain-forest.jpg" />
                  <span>
                     <h4>Frogger</h4>
                     Last night's "Itchy and Scratchy Show" was, without a doubt, the worst episode *ever.*<br/><br/>
                     Rest assured, I was on the Internet within minutes, registering my disgust throughout the world. 
                     <a href="/" title="Link">Click this Link</a>
                  </span>
               </div>
            </li>
            <li>
               <div>
                  <img src="http://1.bp.blogspot.com/_ugaMiBbNfqY/SmhvJ8xHZmI/AAAAAAAAAPs/KGH5oeoE6d8/s400/International+Space+Station.jpg" />
                  <span>
                     <h4>We Have a Problem...</h4>
                     Besides, every time I learn something new, it pushes some old stuff out of my brain.<br/><br/>
                     Remember when I took that home winemaking course, and I forgot how to drive?
                     <a href="/" title="Link">Click this Link</a>
                  </span>
               </div>
            </li>            
         </ul>
         <div class="jcarousel-control">
            <a href="#">1</a>
            <a href="#">2</a>
            <a href="#">3</a>
         </div>
      </div>
   </body>
</html>


			