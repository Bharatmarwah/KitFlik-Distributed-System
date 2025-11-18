package in.bm.MovieManagementService.ENTITY;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "price_details")
@Data
public class PriceDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double baseAmount;
    private Double gst;
    private Double serviceTax;
    private Double totalAmount;
    private Double discount;
    private Double finalAmount;


}


