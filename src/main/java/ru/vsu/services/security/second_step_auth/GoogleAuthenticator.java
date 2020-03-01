package ru.vsu.services.security.second_step_auth;

import de.taimos.totp.TOTP;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Ivan Rovenskiy
 * 01 March 2020
 */
public class GoogleAuthenticator extends AbstractSecondStepAuthenticator<GoogleAuthenticator.Input> {
    @Override
    protected boolean isValid(Input input) {
        return StringUtils.isNumeric(input.getDigitalCode()) && input.getDigitalCode().trim().length() == 6;
    }

    @Override
    protected boolean authoriseImpl(Input input) {
        String code = getTOTPCode(input.getUserSecretKey());

        return code.equals(input.getDigitalCode().trim());
    }

    private static String getTOTPCode(String secretKey) {
        Base32 base32 = new Base32();
        byte[] bytes = base32.decode(secretKey);
        String hexKey = Hex.encodeHexString(bytes);
        return TOTP.getOTP(hexKey);
    }

    public static class Input {
        private String digitalCode;
        private String userSecretKey;

        public Input(String digitalCode, String userSecretKey) {
            this.digitalCode = digitalCode;
            this.userSecretKey = userSecretKey;
        }

        public String getDigitalCode() {
            return digitalCode;
        }

        public String getUserSecretKey() {
            return userSecretKey;
        }
    }
}