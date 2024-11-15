package net.kajilab.elpissender.Presenter.ui.view.Components

import android.os.Build
import android.util.Log
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionDialog() {
    val permissionsState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberMultiplePermissionsState(
            permissions = listOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.BLUETOOTH_SCAN,
                android.Manifest.permission.BLUETOOTH_CONNECT,
                android.Manifest.permission.BLUETOOTH_ADVERTISE,
                android.Manifest.permission.BLUETOOTH_ADMIN,
                android.Manifest.permission.INTERNET,
                android.Manifest.permission.POST_NOTIFICATIONS
            )
        )
    } else {
        rememberMultiplePermissionsState(
            permissions = listOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.BLUETOOTH_ADMIN,
                android.Manifest.permission.INTERNET,
            )
        )
    }

    // パーミッションの状態に応じたUI
    when {
        permissionsState.allPermissionsGranted -> {
            Log.d("LocationPermissionDialog", "allPermissionsGranted")
        }
        permissionsState.shouldShowRationale -> {
            PermissionRationaleDialog(
                onDismissRequest = { permissionsState.launchMultiplePermissionRequest() }
            )
        }
        else -> {
            Text("位置情報のパーミッションが拒否されました")
        }
    }
}

@Composable
fun PermissionRationaleDialog(onDismissRequest: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("このアプリケーションのパーミッションについて") },
        text = {
            Text("アプリで正確な位置情報を取得するために位置情報へのアクセスを許可してください。")
            Text("許可しない場合は、アプリの機能を利用できません。")
            Text("また、通知の許可も必要です。")
        },
        confirmButton = {
            Button(onClick = onDismissRequest) {
                Text("許可する")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("キャンセル")
            }
        }
    )
}
