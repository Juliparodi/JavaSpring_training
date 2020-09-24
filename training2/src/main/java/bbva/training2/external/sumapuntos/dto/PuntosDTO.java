package bbva.training2.external.sumapuntos.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "idPuntos")
public class PuntosDTO implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idPuntos;

    @OneToOne(fetch = FetchType.LAZY, cascade = {
            CascadeType.ALL}, mappedBy = "puntosDTO")
    private DataDTO data;

    private String errors;

    public void addDataDTO(DataDTO dataDTO) {
        this.data = dataDTO;
        dataDTO.setPuntosDTO(this);
    }

    public void removeDataDTO(DataDTO dataDTO) {
        if (dataDTO != null) {
            dataDTO.setPuntosDTO(null);
        }
        this.data = null;
    }

}
