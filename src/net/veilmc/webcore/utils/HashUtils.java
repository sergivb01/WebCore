package net.veilmc.webcore.utils;

import net.veilmc.webcore.Main;

public class HashUtils {

    public String getHash(String passwd){
        return BCrypt.hashpw(passwd, BCrypt.gensalt());
    }

    public boolean checkEquals(String plaintext, String hashed){
        return BCrypt.checkpw(plaintext, hashed);
    }


}
