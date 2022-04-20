package com.minesup.ape.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Type;

/**
 * The Exercise entity.\n@author APE
 */
@Schema(description = "The Exercise entity.\n@author APE")
@Entity
@Table(name = "exercise")
public class Exercise implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * title
     */
    @Schema(description = "title", required = true)
    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    /**
     * content
     */
    @Schema(description = "content", required = true)
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "content", nullable = false)
    private String content;

    /**
     * mark
     */
    @Schema(description = "mark", required = true)
    @NotNull
    @Min(value = 0)
    @Column(name = "mark", nullable = false)
    private Integer mark;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "exercises" }, allowSetters = true)
    private Course course;

    @JsonIgnoreProperties(value = { "exercise" }, allowSetters = true)
    @OneToOne(mappedBy = "exercise")
    private Correction correction;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Exercise id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Exercise title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return this.content;
    }

    public Exercise content(String content) {
        this.setContent(content);
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getMark() {
        return this.mark;
    }

    public Exercise mark(Integer mark) {
        this.setMark(mark);
        return this;
    }

    public void setMark(Integer mark) {
        this.mark = mark;
    }

    public Course getCourse() {
        return this.course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Exercise course(Course course) {
        this.setCourse(course);
        return this;
    }

    public Correction getCorrection() {
        return this.correction;
    }

    public void setCorrection(Correction correction) {
        if (this.correction != null) {
            this.correction.setExercise(null);
        }
        if (correction != null) {
            correction.setExercise(this);
        }
        this.correction = correction;
    }

    public Exercise correction(Correction correction) {
        this.setCorrection(correction);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Exercise)) {
            return false;
        }
        return id != null && id.equals(((Exercise) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Exercise{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", content='" + getContent() + "'" +
            ", mark=" + getMark() +
            "}";
    }
}
