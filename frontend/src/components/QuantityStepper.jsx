/**
 * Increment/decrement control. Decrement stops at 1 — removing a line is an
 * explicit action, not something you fall into by clicking minus.
 */
function QuantityStepper({ value, onChange, disabled, min = 1 }) {
  return (
    <div className="qty-stepper">
      <button
        type="button"
        onClick={() => onChange(value - 1)}
        disabled={disabled || value <= min}
        aria-label="Decrease quantity"
      >
        −
      </button>
      <span className="qty-value" aria-live="polite">
        {value}
      </span>
      <button
        type="button"
        onClick={() => onChange(value + 1)}
        disabled={disabled}
        aria-label="Increase quantity"
      >
        +
      </button>
    </div>
  )
}

export default QuantityStepper
