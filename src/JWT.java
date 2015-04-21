/**
 * Created by asvsfs on 4/21/2015.
 */

import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;

/**
 * Created by asvsfs on 3/21/2015.
 */
public class JWT {

    public static RsaJsonWebKey mJsonWebKey;
    private static boolean bInitialized = false;

    public static void InitializeJWT(){

        if(bInitialized == false) {
            try {
                JWT.mJsonWebKey = RsaJwkGenerator.generateJwk(2048);
                JWT.mJsonWebKey.setKeyId("loginKey");
                bInitialized = true;
            } catch (Exception e) {

            }
        }
    }

    public static String makeJWT(String userid, String date){
        // Create the Claims, which will be the content of the JWT
        try {
            JwtClaims claims = new JwtClaims();
            claims.setGeneratedJwtId(); // a unique identifier for the token
            claims.setIssuedAtToNow();  // when the token was issued/created (now)
            claims.setNotBeforeMinutesInThePast(2); // time before which the token is not yet valid (2 minutes ago)
            claims.setSubject("loginToken"); // the subject/principal is whom the token is about
            claims.setClaim("userid", userid); // additional claims/attributes about the subject can be added
            claims.setClaim("date",date);

            JsonWebSignature jws = new JsonWebSignature();
            jws.setPayload(claims.toJson());
            jws.setKey(mJsonWebKey.getPrivateKey());
            jws.setKeyIdHeaderValue(mJsonWebKey.getKeyId());
            jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);
            String jwt = jws.getCompactSerialization();

            return jwt;
        }catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    public static JwtClaims consumeJWT(String jwt){
        JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                .setRequireSubject()
                .setVerificationKey(mJsonWebKey.getKey())
                .build();

        try
        {
            //  Validate the JWT and process it to the Claims
            JwtClaims jwtClaims = jwtConsumer.processToClaims(jwt);
            System.out.println("JWT validation succeeded! " + jwtClaims);
            return jwtClaims;
        }
        catch (InvalidJwtException e) {
            // InvalidJwtException will be thrown, if the JWT failed processing or validation in anyway.
            // Hopefully with meaningful explanations(s) about what went wrong.
            return null;
        }

    }
}