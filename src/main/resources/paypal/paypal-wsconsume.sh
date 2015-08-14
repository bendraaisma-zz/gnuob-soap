#!/bin/sh
/home/bernard/Tools/wildfly-9.0.0.Final/bin/wsconsume.sh -b paypal-bindings.xml -j paypal-service-121.0.jar PayPalSvc.wsdl
mv output/paypal-service-121.0.jar /home/bernard/Projects/gnuob/gnuob-soap/src/main/webapp/WEB-INF/lib/
rm -Rf output