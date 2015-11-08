package com.netbrasoft.gnuob.generic.customer;

import java.math.BigDecimal;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.netbrasoft.gnuob.generic.AbstractType;

@Cacheable(value = false)
@Entity(name = PostalCode.ENTITY)
@Table(name = PostalCode.TABLE)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@XmlRootElement(name = PostalCode.ENTITY)
public class PostalCode extends AbstractType {

  private static final long serialVersionUID = -3917024445691566656L;

  protected static final String ENTITY = "PostalCode";

  protected static final String TABLE = "GNUOB_POSTAL_CODES";

  /**
   * ISO country code, 2 characters.
   */
  @Column(name = "COUNTRY_CODE", nullable = false)
  private String countryCode;

  @Column(name = "POSTAL_CODE", nullable = false)
  private String postalCode;

  @Column(name = "PLACE_NAME", nullable = false)
  private String placeName;

  /**
   * 1. Order subdivision (state) varchar(100).
   */
  @Column(name = "ADMIN_NAME_1", nullable = false)
  private String adminName1;

  /**
   * 1. Order subdivision (state) varchar(20).
   */
  @Column(name = "ADMIN_CODE_1", nullable = false)
  private String adminCode1;

  /**
   * 2. Order subdivision (state) varchar(100).
   */
  @Column(name = "ADMIN_NAME_2")
  private String adminName2;

  /**
   * 2. Order subdivision (state) varchar(20).
   */
  @Column(name = "ADMIN_CODE_2")
  private String adminCode2;

  /**
   * 3. Order subdivision (state) varchar(100).
   */
  @Column(name = "ADMIN_NAME_3")
  private String adminName3;

  /**
   * 3. Order subdivision (state) varchar(20).
   */
  @Column(name = "ADMIN_CODE_3")
  private String adminCode3;

  /**
   * Estimated latitude (wgs84).
   */
  @Column(name = "LATITUDE", nullable = false)
  private BigDecimal latitude;

  /**
   * Estimated longitude (wgs84).
   */
  @Column(name = "LONGITUDE", nullable = false)
  private BigDecimal longitude;

  /**
   * Accuracy of lat/lng from 1=estimated to 6=centroid.
   */
  @Column(name = "ACCURACY")
  private String accuracy;

  @XmlElement(name = "accuracy")
  public String getAccuracy() {
    return accuracy;
  }

  @XmlElement(name = "adminCode1", required = true)
  public String getAdminCode1() {
    return adminCode1;
  }

  @XmlElement(name = "adminCode2")
  public String getAdminCode2() {
    return adminCode2;
  }

  @XmlElement(name = "adminCode3")
  public String getAdminCode3() {
    return adminCode3;
  }

  @XmlElement(name = "adminName1", required = true)
  public String getAdminName1() {
    return adminName1;
  }

  @XmlElement(name = "adminName2")
  public String getAdminName2() {
    return adminName2;
  }

  @XmlElement(name = "adminName3")
  public String getAdminName3() {
    return adminName3;
  }

  @XmlElement(name = "countryCode", required = true)
  public String getCountryCode() {
    return countryCode;
  }

  @XmlElement(name = "latitude", required = true)
  public BigDecimal getLatitude() {
    return latitude;
  }

  @XmlElement(name = "longitude", required = true)
  public BigDecimal getLongitude() {
    return longitude;
  }

  @XmlElement(name = "placeName", required = true)
  public String getPlaceName() {
    return placeName;
  }

  @XmlElement(name = "postalCode", required = true)
  public String getPostalCode() {
    return postalCode;
  }

  @Override
  public boolean isDetached() {
    return getId() > 0;
  }

  @Override
  public void prePersist() {
    return;
  }

  @Override
  public void preUpdate() {
    return;
  }

  public void setAccuracy(final String accuracy) {
    this.accuracy = accuracy;
  }

  public void setAdminCode1(final String adminCode1) {
    this.adminCode1 = adminCode1;
  }

  public void setAdminCode2(final String adminCode2) {
    this.adminCode2 = adminCode2;
  }

  public void setAdminCode3(final String adminCode3) {
    this.adminCode3 = adminCode3;
  }

  public void setAdminName1(final String adminName1) {
    this.adminName1 = adminName1;
  }

  public void setAdminName2(final String adminName2) {
    this.adminName2 = adminName2;
  }

  public void setAdminName3(final String adminName3) {
    this.adminName3 = adminName3;
  }

  public void setCountryCode(final String countryCode) {
    this.countryCode = countryCode;
  }

  public void setLatitude(final BigDecimal latitude) {
    this.latitude = latitude;
  }

  public void setLongitude(final BigDecimal longitude) {
    this.longitude = longitude;
  }

  public void setPlaceName(final String placeName) {
    this.placeName = placeName;
  }

  public void setPostalCode(final String postalCode) {
    this.postalCode = postalCode;
  }
}
