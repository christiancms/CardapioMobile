package br.com.ezzysoft.cardapiomobile.bean;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by christian on 02/11/17.
 */

public class Usuario implements Serializable {

    public long _id;
    public String login;
    public String password;

    public Usuario() {
    }

    public Usuario(long _id, String login, String password) {
        this._id = _id;
        this.login = login;
        this.password = password;
    }

    public Usuario(long _id, String login) {
        this._id = _id;
        this.login = login;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String criptografaSenha(String senha) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        BigInteger hash = new BigInteger(1, md.digest(senha.getBytes()));
        String s = hash.toString(16);
        if (s.length() % 2 != 0) {
            s = "0" + s;
        }
        //Log.getLog().getLogger(Usuario.class).log(Level.FINEST, String.format("Senha Criptografada: %s",s));
        return s;
    }
}
