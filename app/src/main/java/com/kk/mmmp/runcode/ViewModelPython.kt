package com.kk.mmmp.runcode

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class PythonRepository(private val context: Context) {
    suspend fun copyFileFromAssets(fileName: String, outputFileName: String): String {
        withContext(Dispatchers.IO) {
            // 使用传入的 Context 来调用 copyFileFromAssetsToInternalStorage 方法
            copyFileFromAssetsToInternalStorage(context, fileName, outputFileName)
        }
        return context.getFileStreamPath(outputFileName).absolutePath
    }

    //因为保存图像要用context，所以在这里再写一个方法
    fun saveImageToInternalStorage(imageFileName: String, plt: PyObject): String {
        val imageFile = File(context.filesDir, imageFileName)
        plt.callAttr("savefig", imageFile.absolutePath)
        plt.callAttr("close")
        return imageFile.absolutePath
    }

    //将手机上的文件复制到应用的内部存储，并返回路径，因为python只能读取路径
    fun copyFileToInternalStorage(uri: Uri, fileName: String): String {
        val inputStream = context.contentResolver.openInputStream(uri)
        val directory = File(context.cacheDir, "python_files")
        if (!directory.exists()) directory.mkdirs()
        val file = File(directory, fileName)
        val outputStream = FileOutputStream(file)
        inputStream.use { input ->
            outputStream.use { output ->
                input?.copyTo(output)
            }
        }
        return file.absolutePath
    }
}

class ViewModelFactory(private val pythonRepository: PythonRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewModelPython::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ViewModelPython(pythonRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class ViewModelPython(private val pythonRepository: PythonRepository) : ViewModel(){

    private val _imageFilePath = MutableStateFlow<String?>(null)
    private var _message = MutableStateFlow<String?>(null)
    private val _resultData = MutableStateFlow<List<Double>>(emptyList())
    
    val imageFilePath = _imageFilePath.asStateFlow()
    var message = _message.asStateFlow()
    val resultData = _resultData.asStateFlow()

    fun callPythonCodeAHP(fileUri: Uri) {
        viewModelScope.launch {
            try {
                val python = Python.getInstance()
                val pythonScript = python.getModule("AHP")
                val filePath = pythonRepository.copyFileToInternalStorage(fileUri,"AHP_copy.xlsx")

                val result = pythonScript.callAttr("main", filePath)
                val resultList = result.asList()

                val weights = resultList[0].asList().map { it.toDouble() }
                val ci = resultList[1].toDouble()
                val cr = resultList[2].toDouble()
                val imagePath = resultList[3].toString()

                val consistencyPassed = cr < 0.1
                if (consistencyPassed) {
                    _message.value = "一致性检验通过 (CR=${String.format("%.4f", cr)})\nCI=${String.format("%.4f", ci)}"
                    _imageFilePath.value = imagePath
                    _resultData.value = weights
                } else {
                    _message.value = "一致性未通过 (CR=${String.format("%.4f", cr)})\n请调整比较矩阵"
                    _imageFilePath.value = null
                    _resultData.value = emptyList()
                }
            } catch (e: Exception) {
                Log.e("PythonCodeAHP", "Failed: ${e.message}")
                _message.value = "运行出错: ${e.localizedMessage}"
            }
        }
    }

    fun callPythonCodeEWM(fileUri: Uri) {
        viewModelScope.launch {
            try {
                val python = Python.getInstance()
                val pythonScript = python.getModule("EWM")
                val filePath = pythonRepository.copyFileToInternalStorage(fileUri,"EWM_copy.xlsx")

                val result = pythonScript.callAttr("main", filePath)
                val resultList = result.asList()

                val weights = resultList[0].asList().map { it.toDouble() }
                val imagePath = resultList[1].toString()

                _resultData.value = weights
                _message.value = "熵权法计算完成，结果已生成。"
                _imageFilePath.value = imagePath
            } catch (e: Exception) {
                Log.e("PythonCodeEWM", "Failed: ${e.message}")
                _message.value = "运行出错: ${e.localizedMessage}"
            }
        }
    }

    fun callPythonCodeLOG(fileUri: Uri) {
        viewModelScope.launch {
            try {
                val python = Python.getInstance()
                val pythonScript = python.getModule("Logistic")
                val filePath = pythonRepository.copyFileToInternalStorage(fileUri,"LOG_copy.xlsx")

                val result = pythonScript.callAttr("main", filePath)
                val resultList = result.asList()

                val accuracy = resultList[0].toDouble()
                val cross_val_score = resultList[1].toDouble()
                val imagePath = resultList[2].toString()

                _message.value = "模型准确率: ${String.format("%.2f%%", accuracy * 100)}\n交叉验证得分: ${String.format("%.2f%%", cross_val_score * 100)}"
                _imageFilePath.value = imagePath
                _resultData.value = listOf(accuracy, cross_val_score)
            } catch (e: Exception) {
                Log.e("PythonCodeLOG", "Failed: ${e.message}")
                _message.value = "运行出错: ${e.localizedMessage}"
            }
        }
    }

    fun callPythonCodeTOPSIS(fileUri: Uri) {
        viewModelScope.launch {
            try {
                val python = Python.getInstance()
                val pythonScript = python.getModule("TOPSIS")
                val filePath = pythonRepository.copyFileToInternalStorage(fileUri, "TOPSIS_copy.xlsx")

                val result = pythonScript.callAttr("main", filePath)
                // result is (scores_list, plot_path)
                val resultList = result.asList()
                
                if (resultList[0] == null) {
                     _message.value = "TOPSIS计算失败，可能是数据格式错误。"
                     _imageFilePath.value = null
                     return@launch
                }

                val scores = resultList[0].asList().map { it.toDouble() }
                val imagePath = resultList[1].toString()

                _resultData.value = scores
                _message.value = "TOPSIS评价模型计算完成。"
                _imageFilePath.value = imagePath
            } catch (e: Exception) {
                Log.e("PythonCodeTOPSIS", "Failed: ${e.message}")
                _message.value = "TOPSIS运行出错: ${e.localizedMessage}"
            }
        }
    }

    fun callPythonCodeGREY(fileUri: Uri) {
        viewModelScope.launch {
            try {
                val python = Python.getInstance()
                val pythonScript = python.getModule("GreyPrediction")
                val filePath = pythonRepository.copyFileToInternalStorage(fileUri, "Grey_copy.xlsx")

                val result = pythonScript.callAttr("main", filePath)
                // result is (predicted_list, plot_path)
                val resultList = result.asList()
                
                if (resultList[0] == null) {
                     _message.value = "灰色预测计算失败，可能是数据不满足要求。"
                     _imageFilePath.value = null
                     return@launch
                }

                val predicted = resultList[0].asList().map { it.toDouble() }
                val imagePath = resultList[1].toString()

                _resultData.value = predicted
                _message.value = "灰色预测 GM(1,1) 模型计算完成。"
                _imageFilePath.value = imagePath
            } catch (e: Exception) {
                Log.e("PythonCodeGREY", "Failed: ${e.message}")
                _message.value = "灰色预测运行出错: ${e.localizedMessage}"
            }
        }
    }

    fun callPythonDataPreprocess(fileUri: Uri, mode: String) {
        viewModelScope.launch {
            try {
                val python = Python.getInstance()
                val pythonScript = python.getModule("DataPreprocessing")
                val filePath = pythonRepository.copyFileToInternalStorage(fileUri, "preprocess_input.xlsx")

                val result = pythonScript.callAttr("main", filePath, mode)
                val resultList = result.asList()

                val info = resultList[0].toString()
                val outputPath = resultList[1].toString()

                _message.value = info + "\n\n处理后的文件已保存至临时目录，您可以继续使用该文件进行后续建模。"
                _imageFilePath.value = null 
                _resultData.value = emptyList()
            } catch (e: Exception) {
                Log.e("DataPreprocess", "Failed: ${e.message}")
                _message.value = "预处理出错: ${e.localizedMessage}"
            }
        }
    }
}