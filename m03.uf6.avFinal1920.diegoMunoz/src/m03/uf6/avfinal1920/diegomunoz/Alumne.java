/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m03.uf6.avfinal1920.diegomunoz;

/**
 *
 * @author jmartin
 */
public class Alumne {
  private String nom;
  private String cognoms;
  private String nif;

  public Alumne(String nif, String nom, String cognoms) {
    this.nom = nom;
    this.cognoms = cognoms;
    this.nif = nif;
  }

  public String getNom() {
    return nom;
  }

  public String getCognoms() {
    return cognoms;
  }

  public String getNif() {
    return nif;
  }
}
