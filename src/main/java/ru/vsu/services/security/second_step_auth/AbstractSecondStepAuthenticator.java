package ru.vsu.services.security.second_step_auth;

/**
 * @author Ivan Rovenskiy
 * 01 March 2020
 */
public abstract class AbstractSecondStepAuthenticator<Input> {
    public boolean authorise(Input input) {
        if (isValid(input)) {
            return authoriseImpl(input);
        } else {
            return false;
        }
    }

    protected abstract boolean isValid(Input input);

    protected abstract boolean authoriseImpl(Input input);
}