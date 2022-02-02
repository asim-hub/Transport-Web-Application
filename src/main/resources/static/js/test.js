require([
                                                                                  "esri/config",
                                                                                  "esri/Map",
                                                                                  "esri/views/MapView",

                                                                                  "esri/rest/locator"

                                                                                ], function(esriConfig, Map, MapView, locator) {

                                                                                          esriConfig.apiKey = "AAPKfc023320ea5b4fb9955d7e74a5623fb4KZHfa6kn7i--Jv-1qxWZfL067XASOUpPONXgOqcnsQU8AgpZer-JDLilHLOJL9pY";

                                                                                          const map = new Map({
                                                                                            basemap: "arcgis-navigation"
                                                                                          });

                                                                                          const view = new MapView({
                                                                                            container: "viewDiv_hidden",
                                                                                            map: map,
                                                                                            center: [26.10, 44.43],
                                                                                            zoom: 10
                                                                                          });

                                                                                          const serviceUrl = "http://geocode-api.arcgis.com/arcgis/rest/services/World/GeocodeServer";
                                                                                          var lat = 44.43;
                                                                                          var lng = 26.10;
                                                                                          lat = /*[[${job.truck_lat}]]*/ 44.43;
                                                                                          lng = /*[[${job.truck_lng}]]*/ 26.10;

                                                                                          const point = { //Create a point
                                                                                            type: "point",
                                                                                            longitude: lng,
                                                                                            latitude: lat
                                                                                          };

                                                                                                  const params = {
                                                                                                        location: point
                                                                                                  };

                                                                                                     locator.locationToAddress(serviceUrl, params).then(function(response) { // Show the address found
                                                                                                          const address = response.address;
                                                                                                          document.getElementById('truck_address').innerHTML += "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb";
                                                                                                        }, function(err) { // Show no address found
                                                                                                          document.getElementById("truck_address").innerHTML = "address not found";
                                                                                                        });




                                                                              }



                                                            });