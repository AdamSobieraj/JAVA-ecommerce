package ecom.pl.ecommerce_shop.kafka;

public enum Status {

    NEW("NEW"),
    IMPORTED("IMPORTED"),
    IGNORED("IGNORED"),
    FAILED("FAILED"),
    ROLLED_BACK("ROLLED_BACK");

    private final String status;

    Status(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public static Status getStatus(String status) {
        for (Status s : Status.values()) {
            if (s.getStatus().equals(status)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Invalid status: " + status);
    }


}
