import java.awt.Color
import java.awt.Font
import java.awt.KeyboardFocusManager
import java.awt.Toolkit
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.beans.Visibility
import java.lang.Math.sqrt
import javax.lang.model.util.Types
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener
import javax.swing.text.View

class MyCalculator : JFrame(), MouseListener, DocumentListener {
    val dimension = Toolkit.getDefaultToolkit().screenSize
    val buttons = arrayOfNulls<JLabel>(24)
    var text1 = "0"
    var text2 = ""
    var number1 = 0.0
    var number2 = 0.0
    var fieldPanel = JPanel()
    var c = ' '
    var calcPanel = calcPanel()
    var fieldPanelLabel = JLabel()
    var fieldPanelField = JTextField()

    init {
        val mainPanel = mainPanel()
        mainPanel.add(calcPanel)
        mainPanel.add(memory())
        mainPanel.add(line1())
        calcPanel.add(line())
        fieldPanel = createFieldPanel()
        calcPanel.add(fieldPanel)
        //
        createCalcArguments()

        add(mainPanel)
        extendedState = MAXIMIZED_BOTH
        isResizable = false
        defaultCloseOperation = EXIT_ON_CLOSE
        isVisible = true
    }


    override fun mouseClicked(e: MouseEvent?) {
    }

    override fun mousePressed(e: MouseEvent?) {
        for (button in buttons) {
            if (e?.source == button) {
                if (button?.text.toString()[0] in '0'..'9' && button?.text.toString().length == 1) {
                    if (text1 == "0") text1 = ""
                    if (text2 == "0") text2 = ""
                    text1 = text1.plus(button?.text.toString())
                    fieldPanelField.text = text1
                } else {
                    when (button?.text.toString()) {
                        "<-" -> {
                            if (text1.isNotEmpty())
                                text1 = text1.substring(0, text1.length - 1)
                            if (text1.isEmpty())
                                text1 = "0"
                            fieldPanelField.text = text1
                        }
                        "C" -> {
                            text1 = "0"
                            text2 = ""
                            fieldPanelField.text = text1
                            fieldPanelLabel.text = text2
                            number1 = 0.0
                            number2 = 0.0
                        }
                        "CE" -> {
                            text1 = "0"
                            fieldPanelField.text = text1
                            number2 = 0.0
                        }
                        "%" -> {
                            number1 = text1.toDouble()
                            fieldPanelLabel.text = ""
                            if (types(number1 / 100))
                                fieldPanelField.text = ((number1.toInt() / 100)).toString()
                            else
                                fieldPanelField.text = ((number1 / 100.0)).toString()
                            text1 = ""
                        }
                        "+" -> {
                            if (number1 == 0.0) {
                                number1 = text1.toDouble()
                                if (types(number1))
                                    text2 = "${number1.toInt()} + "
                                else
                                    text2 = "$number1 + "
                                fieldPanelLabel.text = text2
                                c = '+'
                            } else {
                                number2 = text1.toDouble()
                                calculator(c)
                                if (types(number2))
                                    text2 += "${number2.toInt()} + "
                                else
                                    text2 += "$number2 + "
                                fieldPanelLabel.text = text2

                                if (types(number1))
                                    fieldPanelField.text = number1.toInt().toString()
                                else
                                    fieldPanelField.text = number1.toString()
                                c = '+'
                            }
                            text1 = ""
                        }
                        "-" -> {
                            if (number1 == 0.0) {
                                number1 = text1.toDouble()
                                if (types(number1))
                                    text2 = "${number1.toInt()} - "
                                else
                                    text2 = "$number1 - "
                                fieldPanelLabel.text = text2
                                c = '-'
                            } else {
                                number2 = text1.toDouble()
                                calculator(c)
                                if (types(number2))
                                    text2 += "${number2.toInt()} - "
                                else
                                    text2 += "$number2 - "
                                fieldPanelLabel.text = text2
                                if (types(number1))
                                    fieldPanelField.text = number1.toInt().toString()
                                else
                                    fieldPanelField.text = number1.toString()
                                c = '-'
                            }
                            text1 = ""
                        }
                        "*" -> {
                            if (number1 == 0.0) {
                                number1 = text1.toDouble()
                                if (types(number1))
                                    text2 = "${number1.toInt()} * "
                                else
                                    text2 = "$number1 * "
                                fieldPanelLabel.text = text2
                                c = '*'
                            } else {
                                number2 = text1.toDouble()
                                calculator(c)
                                if (types(number2))
                                    text2 += "${number2.toInt()} * "
                                else
                                    text2 += "$number2 * "
                                fieldPanelLabel.text = text2
                                if (types(number1))
                                    fieldPanelField.text = number1.toInt().toString()
                                else
                                    fieldPanelField.text = number1.toString()
                                c = '*'
                            }
                            text1 = ""
                        }
                        "/" -> {
                            if (number1 == 0.0) {
                                number1 = text1.toDouble()
                                if (types(number1))
                                    text2 = "${number1.toInt()} / "
                                else
                                    text2 = "$number1 / "
                                fieldPanelLabel.text = text2
                                c = '/'
                            } else {
                                number2 = text1.toDouble()
                                calculator(c)
                                if (types(number1))
                                    text2 += "${number2.toInt()} / "
                                else
                                    text2 += "$number2 / "
                                fieldPanelLabel.text = text2
                                if (types(number1))
                                    fieldPanelField.text = number1.toInt().toString()
                                else
                                    fieldPanelField.text = number1.toString()
                                c = '/'
                            }
                            text1 = ""
                        }
                        "=" -> {
                            if (text1.isEmpty()) {
                                text1 = "0"
                            }
                            number2 = text1.toDouble()
                            calculator(c)
                            if (types(number1))
                                fieldPanelField.text = "${number1.toInt()}"
                            else
                                fieldPanelField.text = "${number1}"
                            fieldPanelLabel.text = ""

                        }
                        "+,-" -> {

                            if (text1.isEmpty()) {
                                text1 = "0"
                            }
                            number1 = text1.toDouble()
                            number1 *= -1
                            if (types(number1))
                                text1 = number1.toInt().toString()
                            else
                                text1 = number1.toString()
                            fieldPanelField.text = text1
                        }
                        "√" -> {
                            number1 = text1.toDouble()
                            if (types(number1))
                                text2 = "${sqrt(number1).toInt()}"
                            else
                                text2 = "${sqrt(number1.toDouble())}"
                            fieldPanelLabel.text = "sqrt($number1)"
                            if (types(number1))
                                fieldPanelField.text = Math.sqrt(number1).toString()
                            else
                                fieldPanelField.text = Math.sqrt(number1).toString()
                            text1 = ""
                        }
                        "1/x" -> {
                            number1 = text1.toDouble()
                            fieldPanelLabel.text = "reciproc($number1)"
                            if (types(number1))
                                fieldPanelField.text = (1.0 / number1.toInt()).toString()
                            else
                                fieldPanelField.text = (1.0 / number1).toString()
                        }
                        "." -> {
                            if (!text1.contains("."))
                                text1 = text1.plus(".")
                            fieldPanelField.text = text1
                        }

                    }
                }
                break
            }
        }
    }

    private fun types(value: Double) = (value.toInt()).toDouble() == value
    private fun calculator(c: Char) {
        when (c) {
            '+' -> {
                number1 += number2
            }
            '-' -> {
                number1 -= number2
            }
            '*' -> {
                number1 *= number2
            }
            '/' -> {
                number1 /= number2
            }
        }
    }

    override fun mouseReleased(e: MouseEvent?) {
    }

    override fun mouseEntered(e: MouseEvent?) {
        for (button in buttons) {
            if (e?.source == button) {
                if (button?.text.toString() == "=")
                    button?.background = Color.GREEN
                else
                    button?.background = Color.GRAY
                break
            }
        }
    }

    override fun mouseExited(e: MouseEvent?) {
        for (button in buttons) {
            if (e?.source == button) {
                if (button?.text.toString() == "=")
                    button?.background = Color.GREEN
                else
                    button?.background = Color.DARK_GRAY
                break
            }
        }
    }

    private fun mainPanel(): JPanel {
        val mainPanel = JPanel()
        mainPanel.setSize(dimension.width, dimension.height)
        mainPanel.background = Color.decode("#0212500")
        mainPanel.layout = null
        mainPanel.alignmentX = JPanel.CENTER_ALIGNMENT
        mainPanel.alignmentY = JPanel.CENTER_ALIGNMENT

        return mainPanel
    }

    private fun calcPanel(): JPanel {
        val panel = JPanel()
        panel.setBounds((dimension.width - 700) / 2, (dimension.height - 520) / 2, 700, 520)
        panel.isOpaque = true
        panel.background = Color.decode("#0212500")
        panel.layout = null

        return panel
    }

    private fun line(): JLabel {
        val line = JLabel()
        line.setBounds(0, 0, 700, 5)
        line.isOpaque = true
        line.background = Color.GREEN

        return line
    }

    private fun memory(): JLabel {
        val memory = JLabel()
        memory.setBounds(1050, 280, 100, 365)
        memory.isOpaque = true
        memory.background = Color.DARK_GRAY
        memory.isVisible = false
        return memory
    }

    private fun line1(): JLabel {
        val line1 = JLabel()
        line1.setBounds(1050, 240, 100, 20)
        line1.background = Color.WHITE
        line1.text = "Memory"
        line1.foreground = Color.WHITE
        line1.isVisible = false
        return line1

    }

    private fun values() =
        arrayOf(
            "M",
            "CE",
            "C",
            "<-",
            "/",
            "+,-",
            "7",
            "8",
            "9",
            "*",
            "√",
            "4",
            "5",
            "6",
            "-",
            "%",
            "1",
            "2",
            "3",
            "+",
            "1/x",
            "0",
            ".",
            "="
        )

    private fun createFieldPanel(): JPanel {
        val fieldPanel = JPanel()
        fieldPanel.layout = null
        fieldPanel.setBounds(0, 5, 700, 135)
        fieldPanel.background = Color.DARK_GRAY

        fieldPanelLabel.foreground = Color.LIGHT_GRAY
        fieldPanelLabel.font = Font("Arial", Font.BOLD, 20)
        fieldPanelLabel.setBounds(20, 20, 660, 30)
        fieldPanelLabel.horizontalAlignment = JLabel.RIGHT
        fieldPanelLabel.verticalAlignment = JLabel.CENTER
        fieldPanel.add(fieldPanelLabel)

        fieldPanelField.text = "0"
        fieldPanelField.border = null
        fieldPanelField.foreground = Color.WHITE
        fieldPanelField.font = Font("Arial", Font.BOLD, 40)
        fieldPanelField.setBounds(20, 50, 660, 85)
        fieldPanelField.horizontalAlignment = JLabel.RIGHT
        fieldPanelField.background = Color.DARK_GRAY
        fieldPanelField.caretColor = Color.WHITE
        fieldPanelField.document.addDocumentListener(this)
//        fieldPanelField.border = BorderFactory.createCompoundBorder(fieldPanelField.border, BorderFactory.createEmptyBorder(50, 0, 0, 10))
        fieldPanel.add(fieldPanelField)

        return fieldPanel
    }

    private fun createCalcArguments() {
        val values = values()
        var x = 0
        var y = 155
        var width = 134
        val height = 70
        for (i in buttons.indices) {
            if (i % 5 == 0 && i != 0) {
                x = 0
                y += 75
            }
            if (i == 21) {
                width = 2 * width + 5
            } else width = 134
            val button = JLabel()
            button.setBounds(x, y, width, height)
            button.text = values[i]
            button.font = Font("Areal", Font.BOLD, 25)
            button.isOpaque = true
            button.horizontalAlignment = JLabel.CENTER
            button.background = Color.DARK_GRAY
            button.foreground = Color.WHITE
            button.addMouseListener(this)
            if (i == 23) {
                button.background = Color.GREEN
            }
            buttons[i] = button
            calcPanel.add(button)
            x += 139
            if (i == 21) x += 139
            if (i == 0 || i == 5 || i == 10 || i == 15 || i == 20) x += 10


        }
    }

    override fun insertUpdate(e: DocumentEvent?) {
        checkChangeField()
        println("insertUpdate: ${e}")
    }

    override fun removeUpdate(e: DocumentEvent?) {
        checkChangeField()
        println("removeUpdate: ${e.toString()}")
    }

    override fun changedUpdate(e: DocumentEvent?) {
        checkChangeField()
        println("Change: ${e.toString()}")
    }

    private fun checkChangeField() {
        println(fieldPanelField.text)
        var result = fieldPanelField.text.toString()
//        if (result.length == 2) {
//            if (result[1] == '0')
//                result = result[0].toString()
//        }
//        if (fieldPanelField.text.toString() == "0") {
//            result = result.substring(0, result.length - 1)
//            fieldPanelField.text = result
//        } else {
//
//        }
        if (result.length == 2){
            fieldPanelField.text = "yes"
        }


//        fieldPanelField.text = result
//        val result =
//            try {
//                fieldPanelField.text.toDouble()
//            } catch (e: java.lang.Exception) {
//                fieldPanelField.text.substring(0, fieldPanelField.text.toString().length - 1)
//            }
//        fieldPanelField.text = result.toString()
    }
}


fun main() {
    MyCalculator()
}
