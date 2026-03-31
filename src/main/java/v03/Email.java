package v03;

import java.util.Objects;

public final class Email {
    private final String address;

    public Email(String address) {
        if (address == null || !address.contains("@") || address.indexOf("@") == 0 || address.indexOf("@") == address.length() - 1) {
            throw new IllegalArgumentException("Invalid email address");
        }
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email = (Email) o;
        return Objects.equals(address, email.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address);
    }

    @Override
    public String toString() {
        return "Email{" +
                "address='" + address + '\'' +
                '}';
    }
}
