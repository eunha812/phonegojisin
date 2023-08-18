package com.ssafy.phonesin.ui.mobile.returnmobile

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.ssafy.phonesin.ApplicationClass
import com.ssafy.phonesin.R
import com.ssafy.phonesin.databinding.FragmentReturnAgentBinding
import com.ssafy.phonesin.ui.mobile.AgentAdapter
import com.ssafy.phonesin.ui.mobile.MobileViewModel
import com.ssafy.phonesin.ui.util.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ReturnAgentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class ReturnAgentFragment :
    BaseFragment<FragmentReturnAgentBinding>(R.layout.fragment_return_agent) {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    //TODO DonateAgentFragment와 함께 코드정리 필요

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    val returnAgentViewModel: MobileViewModel by viewModels()
    lateinit var current: Location

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        // 위치 권한 요청
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // 권한이 없을 경우 권한 요청
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                ApplicationClass.PERMISSIONS_REQUEST_LOCATION
            )
        } else {
            // 권한이 있을 경우 위치 업데이트 요청
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    // 위치 정보를 가져왔을 때 처리하는 로직
                    if (location != null) {
                        current = location
                        returnAgentViewModel.getAgentAddress(current.latitude, current.longitude)

                        // TODO: 위도(latitude)와 경도(longitude)를 이용해 원하는 작업 수행
                        // 예를 들어, 지도에 현재 위치 표시 등
                    }
                }
                .addOnFailureListener { exception ->
                    // 위치 정보 가져오기 실패 처리
                    // 예외 처리 등
                }
        }
    }

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentReturnAgentBinding {
        return FragmentReturnAgentBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
    }

    override fun init() {
        setReturnAgentUi()
        setReturnAdapter()
    }

    private fun setReturnAdapter() {
        val agentAdapter = AgentAdapter()
        agentAdapter.detailAgentListener = object :
            AgentAdapter.DetailAgentListener {
            override fun onClick(id: Int) {
                val data = returnAgentViewModel.agentAddress.value?.get(id)
                val bundle = bundleOf()
                bundle.putString("address", data?.agencyLocation)
                bundle.putString("name", data?.agencyName)
                data?.let { bundle.putDouble("longitude", it.agencyX) }
                data?.let { bundle.putDouble("latitude", it.agencyY) }

                findNavController().navigate(
                    R.id.action_returnAgentFragment_to_returnAgentDetailFragment, bundle
                )
            }

        }

        bindingNonNull.recyclerViewReturnAgentAddress.adapter = agentAdapter

        returnAgentViewModel.agentAddress.observe(viewLifecycleOwner) {
            agentAdapter.submitList(it)
        }
    }


    private fun setReturnAgentUi() = with(bindingNonNull) {
        searchViewReturnAgent.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // 검색 버튼을 눌렀을 때 호출되는 콜백
                // 여기서 검색어(query)를 이용하여 검색 작업을 수행하면 됩니다.
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                // 검색어가 변경될 때마다 호출되는 콜백
                // 여기서 변경된 검색어(newText)를 이용하여 실시간 검색 기능을 구현할 수 있습니다.
                if (newText == "") {
                    returnAgentViewModel.getAgentAddress(current.latitude, current.longitude)
                }
                returnAgentViewModel.getSearchAgentAddress(
                    current.latitude,
                    current.longitude,
                    newText
                )
                return true
            }
        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ReturnAgentFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ReturnAgentFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}