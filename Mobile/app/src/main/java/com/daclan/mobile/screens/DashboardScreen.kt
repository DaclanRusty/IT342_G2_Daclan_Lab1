package com.daclan.mobile.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daclan.mobile.network.RetrofitClient
import com.daclan.mobile.util.Prefs
import androidx.compose.material3.Divider



@Composable
fun DashboardScreen(onLoggedOut: () -> Unit) {
    val context = LocalContext.current

    var fullName by remember { mutableStateOf("—") }
    var email by remember { mutableStateOf("—") }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        val token = Prefs.getBearerToken(context) ?: run { onLoggedOut(); return@LaunchedEffect }
        try {
            val response = RetrofitClient.api.getProfile(token)
            if (response.isSuccessful) {
                val user = response.body()!!
                fullName = "${user.firstName} ${user.lastName}"
                email = user.email
            } else if (response.code() == 401) {
                Prefs.clear(context)
                onLoggedOut()
            }
        } catch (e: Exception) {
            val fn = Prefs.getFirstName(context) ?: ""
            val ln = Prefs.getLastName(context) ?: ""
            if (fn.isNotEmpty()) fullName = "$fn $ln"
            val em = Prefs.getEmail(context) ?: ""
            if (em.isNotEmpty()) email = em
        } finally {
            isLoading = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1A2332),
                        Color(0xFF2D4A6F),
                        Color(0xFF5A7A9E),
                        Color(0xFFD4917E),
                        Color(0xFF7DA3C6)
                    )
                )
            )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // ── Nav Bar ──
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0x0DFFFFFF))
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("User Dashboard", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)

                OutlinedButton(
                    onClick = { Prefs.clear(context); onLoggedOut() },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                    border = BorderStroke(1.dp, Color(0x4DFFFFFF))
                ) {
                    Text("🚪 Logout", color = Color.White, fontSize = 13.sp)
                }
            }

            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color.White)
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {

                    // ── Welcome Card ──
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0x1FFFFFFF)),
                        elevation = CardDefaults.cardElevation(12.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(36.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("✅", fontSize = 52.sp, modifier = Modifier.padding(bottom = 12.dp))
                            Text("Welcome Back!", fontSize = 26.sp, fontWeight = FontWeight.Bold,
                                color = Color.White, modifier = Modifier.padding(bottom = 8.dp))
                            Text("You are successfully logged in", fontSize = 15.sp, color = Color(0xCCFFFFFF))
                        }
                    }

                    // ── Profile Card ──
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0x1FFFFFFF)),
                        elevation = CardDefaults.cardElevation(12.dp)
                    ) {
                        Column(modifier = Modifier.fillMaxWidth().padding(28.dp)) {

                            Column(
                                modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("👤", fontSize = 56.sp, modifier = Modifier.padding(bottom = 12.dp))
                                Text("Profile Information", fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold, color = Color.White)
                            }

                            Divider(
                                color = Color(0x33FFFFFF),
                                thickness = 1.dp
                            )


                            // Full Name
                            InfoRow(icon = "👤", label = "Full Name", value = fullName)
                            Spacer(modifier = Modifier.height(12.dp))

                            // Email
                            InfoRow(icon = "✉", label = "Email Address", value = email)
                            Spacer(modifier = Modifier.height(12.dp))

                            // Status
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0x14FFFFFF), RoundedCornerShape(12.dp))
                                    .padding(18.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("✅  ", fontSize = 15.sp)
                                    Text("Account Status", fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold, color = Color(0xCCFFFFFF))
                                }
                                Surface(shape = RoundedCornerShape(20.dp), color = Color(0x4D10B981)) {
                                    Text("● Active", color = Color(0xFF6EE7B7), fontSize = 13.sp,
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 5.dp))
                                }
                            }
                        }
                    }

                    // ── Stat Cards ──
                    StatCard(icon = "👤", iconBg = Color(0x4D3B82F6), label = "ACCOUNT TYPE", value = "Standard User")
                    StatCard(icon = "✅", iconBg = Color(0x4D10B981), label = "VERIFICATION", value = "Verified")
                    StatCard(icon = "✉", iconBg = Color(0x4D6366F1), label = "EMAIL STATUS", value = "Confirmed")

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun InfoRow(icon: String, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0x14FFFFFF), RoundedCornerShape(12.dp))
            .padding(18.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("$icon  ", fontSize = 15.sp)
            Text(label, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xCCFFFFFF))
        }
        Text(value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
    }
}

@Composable
fun StatCard(icon: String, iconBg: Color, label: String, value: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0x1FFFFFFF)),
        elevation = CardDefaults.cardElevation(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(modifier = Modifier.size(56.dp), shape = RoundedCornerShape(12.dp), color = iconBg) {
                Box(contentAlignment = Alignment.Center) {
                    Text(icon, fontSize = 24.sp)
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(label, fontSize = 11.sp, fontWeight = FontWeight.Bold,
                    color = Color(0xB3FFFFFF), modifier = Modifier.padding(bottom = 4.dp))
                Text(value, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}