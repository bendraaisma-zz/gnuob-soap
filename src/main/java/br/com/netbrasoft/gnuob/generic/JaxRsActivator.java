package br.com.netbrasoft.gnuob.generic;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import com.fasterxml.jackson.databind.ObjectMapper;

@ApplicationPath("/")
public class JaxRsActivator extends Application {
  public static final ObjectMapper mapper = new ObjectMapper();
}
