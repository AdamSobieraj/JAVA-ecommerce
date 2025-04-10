package ecom.pl.ecommerce_shop.database;

import ecom.pl.ecommerce_shop.kafka.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "data_import")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataImport {

    @Id
    private UUID id;

    @Column
    private LocalDateTime timestamp;

    @Column
    private String dataUrl;

    @Column
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column
    private String issuer;

    @Column
    private String statusInformation;

}
