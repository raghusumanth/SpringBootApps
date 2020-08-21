/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package com.kafka.healthcheck.model;

import org.apache.avro.specific.SpecificData;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.SchemaStore;

@SuppressWarnings("all")
@org.apache.avro.specific.AvroGenerated
public class AvroEvent extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = -1065369572477659519L;
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"AvroEvent\",\"namespace\":\"com.kafka.healthcheck.model\",\"fields\":[{\"name\":\"message\",\"type\":\"string\"},{\"name\":\"ids\",\"type\":\"int\"},{\"name\":\"hello\",\"type\":\"string\"}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<AvroEvent> ENCODER =
      new BinaryMessageEncoder<AvroEvent>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<AvroEvent> DECODER =
      new BinaryMessageDecoder<AvroEvent>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   */
  public static BinaryMessageDecoder<AvroEvent> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   */
  public static BinaryMessageDecoder<AvroEvent> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<AvroEvent>(MODEL$, SCHEMA$, resolver);
  }

  /** Serializes this AvroEvent to a ByteBuffer. */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /** Deserializes a AvroEvent from a ByteBuffer. */
  public static AvroEvent fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

  @Deprecated public java.lang.CharSequence message;
  @Deprecated public int ids;
  @Deprecated public java.lang.CharSequence hello;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public AvroEvent() {}

  /**
   * All-args constructor.
   * @param message The new value for message
   * @param ids The new value for ids
   * @param hello The new value for hello
   */
  public AvroEvent(java.lang.CharSequence message, java.lang.Integer ids, java.lang.CharSequence hello) {
    this.message = message;
    this.ids = ids;
    this.hello = hello;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call.
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return message;
    case 1: return ids;
    case 2: return hello;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  // Used by DatumReader.  Applications should not call.
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: message = (java.lang.CharSequence)value$; break;
    case 1: ids = (java.lang.Integer)value$; break;
    case 2: hello = (java.lang.CharSequence)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'message' field.
   * @return The value of the 'message' field.
   */
  public java.lang.CharSequence getMessage() {
    return message;
  }

  /**
   * Sets the value of the 'message' field.
   * @param value the value to set.
   */
  public void setMessage(java.lang.CharSequence value) {
    this.message = value;
  }

  /**
   * Gets the value of the 'ids' field.
   * @return The value of the 'ids' field.
   */
  public java.lang.Integer getIds() {
    return ids;
  }

  /**
   * Sets the value of the 'ids' field.
   * @param value the value to set.
   */
  public void setIds(java.lang.Integer value) {
    this.ids = value;
  }

  /**
   * Gets the value of the 'hello' field.
   * @return The value of the 'hello' field.
   */
  public java.lang.CharSequence getHello() {
    return hello;
  }

  /**
   * Sets the value of the 'hello' field.
   * @param value the value to set.
   */
  public void setHello(java.lang.CharSequence value) {
    this.hello = value;
  }

  /**
   * Creates a new AvroEvent RecordBuilder.
   * @return A new AvroEvent RecordBuilder
   */
  public static com.kafka.healthcheck.model.AvroEvent.Builder newBuilder() {
    return new com.kafka.healthcheck.model.AvroEvent.Builder();
  }

  /**
   * Creates a new AvroEvent RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new AvroEvent RecordBuilder
   */
  public static com.kafka.healthcheck.model.AvroEvent.Builder newBuilder(com.kafka.healthcheck.model.AvroEvent.Builder other) {
    return new com.kafka.healthcheck.model.AvroEvent.Builder(other);
  }

  /**
   * Creates a new AvroEvent RecordBuilder by copying an existing AvroEvent instance.
   * @param other The existing instance to copy.
   * @return A new AvroEvent RecordBuilder
   */
  public static com.kafka.healthcheck.model.AvroEvent.Builder newBuilder(com.kafka.healthcheck.model.AvroEvent other) {
    return new com.kafka.healthcheck.model.AvroEvent.Builder(other);
  }

  /**
   * RecordBuilder for AvroEvent instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<AvroEvent>
    implements org.apache.avro.data.RecordBuilder<AvroEvent> {

    private java.lang.CharSequence message;
    private int ids;
    private java.lang.CharSequence hello;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(com.kafka.healthcheck.model.AvroEvent.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.message)) {
        this.message = data().deepCopy(fields()[0].schema(), other.message);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.ids)) {
        this.ids = data().deepCopy(fields()[1].schema(), other.ids);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.hello)) {
        this.hello = data().deepCopy(fields()[2].schema(), other.hello);
        fieldSetFlags()[2] = true;
      }
    }

    /**
     * Creates a Builder by copying an existing AvroEvent instance
     * @param other The existing instance to copy.
     */
    private Builder(com.kafka.healthcheck.model.AvroEvent other) {
            super(SCHEMA$);
      if (isValidValue(fields()[0], other.message)) {
        this.message = data().deepCopy(fields()[0].schema(), other.message);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.ids)) {
        this.ids = data().deepCopy(fields()[1].schema(), other.ids);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.hello)) {
        this.hello = data().deepCopy(fields()[2].schema(), other.hello);
        fieldSetFlags()[2] = true;
      }
    }

    /**
      * Gets the value of the 'message' field.
      * @return The value.
      */
    public java.lang.CharSequence getMessage() {
      return message;
    }

    /**
      * Sets the value of the 'message' field.
      * @param value The value of 'message'.
      * @return This builder.
      */
    public com.kafka.healthcheck.model.AvroEvent.Builder setMessage(java.lang.CharSequence value) {
      validate(fields()[0], value);
      this.message = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'message' field has been set.
      * @return True if the 'message' field has been set, false otherwise.
      */
    public boolean hasMessage() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'message' field.
      * @return This builder.
      */
    public com.kafka.healthcheck.model.AvroEvent.Builder clearMessage() {
      message = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'ids' field.
      * @return The value.
      */
    public java.lang.Integer getIds() {
      return ids;
    }

    /**
      * Sets the value of the 'ids' field.
      * @param value The value of 'ids'.
      * @return This builder.
      */
    public com.kafka.healthcheck.model.AvroEvent.Builder setIds(int value) {
      validate(fields()[1], value);
      this.ids = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'ids' field has been set.
      * @return True if the 'ids' field has been set, false otherwise.
      */
    public boolean hasIds() {
      return fieldSetFlags()[1];
    }


    /**
      * Clears the value of the 'ids' field.
      * @return This builder.
      */
    public com.kafka.healthcheck.model.AvroEvent.Builder clearIds() {
      fieldSetFlags()[1] = false;
      return this;
    }

    /**
      * Gets the value of the 'hello' field.
      * @return The value.
      */
    public java.lang.CharSequence getHello() {
      return hello;
    }

    /**
      * Sets the value of the 'hello' field.
      * @param value The value of 'hello'.
      * @return This builder.
      */
    public com.kafka.healthcheck.model.AvroEvent.Builder setHello(java.lang.CharSequence value) {
      validate(fields()[2], value);
      this.hello = value;
      fieldSetFlags()[2] = true;
      return this;
    }

    /**
      * Checks whether the 'hello' field has been set.
      * @return True if the 'hello' field has been set, false otherwise.
      */
    public boolean hasHello() {
      return fieldSetFlags()[2];
    }


    /**
      * Clears the value of the 'hello' field.
      * @return This builder.
      */
    public com.kafka.healthcheck.model.AvroEvent.Builder clearHello() {
      hello = null;
      fieldSetFlags()[2] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public AvroEvent build() {
      try {
        AvroEvent record = new AvroEvent();
        record.message = fieldSetFlags()[0] ? this.message : (java.lang.CharSequence) defaultValue(fields()[0]);
        record.ids = fieldSetFlags()[1] ? this.ids : (java.lang.Integer) defaultValue(fields()[1]);
        record.hello = fieldSetFlags()[2] ? this.hello : (java.lang.CharSequence) defaultValue(fields()[2]);
        return record;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<AvroEvent>
    WRITER$ = (org.apache.avro.io.DatumWriter<AvroEvent>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<AvroEvent>
    READER$ = (org.apache.avro.io.DatumReader<AvroEvent>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

}