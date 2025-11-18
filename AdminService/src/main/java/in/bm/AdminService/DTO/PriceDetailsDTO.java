package in.bm.AdminService.DTO;

import lombok.Data;

@Data
public class PriceDetailsDTO {

    private Long id;

    private Double baseAmount;
    private Double gst;
    private Double serviceTax;
    private Double totalAmount;
    private Double discount;
    private Double finalAmount;
}
